package com.hoatv.metric.mgmt.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoatv.metric.mgmt.annotations.MetricConsumer;
import com.hoatv.metric.mgmt.config.KafkaMetricConfig;
import com.hoatv.metric.mgmt.entities.ComplexValue;
import com.hoatv.metric.mgmt.entities.MetricTag;
import com.hoatv.metric.mgmt.entities.SimpleValue;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.Future;

@MetricConsumer
public class KafkaMetricConsumer implements MetricConsumerHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMetricConsumer.class);
    private static final String DEFAULT_TOPIC = "metrics";
    private static final String DEFAULT_BOOTSTRAP_SERVERS = "localhost:9092";
    
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private final String topicName;
    
    public KafkaMetricConsumer() {
        this(new KafkaMetricConfig());
    }
    
    public KafkaMetricConsumer(String bootstrapServers, String topicName) {
        this(new KafkaMetricConfig(bootstrapServers, topicName));
    }
    
    public KafkaMetricConsumer(KafkaMetricConfig config) {
        this.topicName = config.getTopicName();
        this.objectMapper = new ObjectMapper();
        
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, config.getAcks());
        props.put(ProducerConfig.RETRIES_CONFIG, config.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, config.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, config.getLingerMs());
        
        this.producer = new KafkaProducer<>(props);
        
        LOGGER.info("KafkaMetricConsumer initialized with bootstrap servers: {} and topic: {}", 
                   config.getBootstrapServers(), topicName);
    }

    @Override
    public void consume(String application, String category, String name, Object value, String unit) {
        try {
            if (value instanceof SimpleValue) {
                processSimpleValue(application, category, name, (SimpleValue) value, unit);
            } else if (value instanceof String) {
                String metricValue = (String) value;
                try {
                    Long metricValueLong = NumberUtils.createLong(metricValue);
                    processSimpleValue(application, category, name, metricValueLong, unit);
                } catch (NumberFormatException exception) {
                    Double metricValueDouble = NumberUtils.createDouble(metricValue);
                    processSimpleValue(application, category, name, metricValueDouble, unit);
                }
            } else if (value instanceof Long) {
                processSimpleValue(application, category, name, (Long) value, unit);
            } else if (value instanceof Integer) {
                Integer metricValue = (Integer) value;
                processSimpleValue(application, category, name, metricValue.longValue(), unit);
            } else if (value instanceof Double) {
                processSimpleValue(application, category, name, (Double) value, unit);
            } else if (value instanceof Collection) {
                @SuppressWarnings("unchecked")
                Collection<ComplexValue> complexValues = (Collection<ComplexValue>) value;
                complexValues.forEach(complexValue -> processComplexValue(application, category, name, complexValue, unit));
            } else {
                processComplexValue(application, category, name, (ComplexValue) value, unit);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing metric: application={}, category={}, name={}, value={}, unit={}", 
                        application, category, name, value, unit, e);
        }
    }
    
    private void processSimpleValue(String application, String category, String name, SimpleValue simpleValue, String unit) {
        sendMetricToKafka(application, category, name, simpleValue.getValue(), unit, Collections.emptyMap());
    }
    
    private void processSimpleValue(String application, String category, String name, Object value, String unit) {
        sendMetricToKafka(application, category, name, value, unit, Collections.emptyMap());
    }
    
    private void processComplexValue(String application, String category, String name, ComplexValue complexValue, String unit) {
        if (complexValue == null || complexValue.getTags() == null) {
            LOGGER.warn("ComplexValue or its tags are null for metric: {}", name);
            return;
        }
        
        Collection<MetricTag> metricTags = complexValue.getTags();
        for (MetricTag metricTag : metricTags) {
            Map<String, String> attributes = metricTag.getAttributes();
            String effectiveUnit = getEffectiveUnit(unit, attributes);
            String effectiveName = getEffectiveName(name, attributes);
            
            try {
                Object metricValue = parseMetricValue(metricTag.getValue());
                sendMetricToKafka(application, category, effectiveName, metricValue, effectiveUnit, attributes);
            } catch (NumberFormatException e) {
                LOGGER.warn("Failed to parse metric value '{}' for metric '{}'", metricTag.getValue(), name);
                sendMetricToKafka(application, category, effectiveName, metricTag.getValue(), effectiveUnit, attributes);
            }
        }
    }
    
    private String getEffectiveUnit(String defaultUnit, Map<String, String> attributes) {
        String unitAttribute = attributes.get("unit");
        return unitAttribute != null ? unitAttribute : defaultUnit;
    }
    
    private String getEffectiveName(String defaultName, Map<String, String> attributes) {
        String nameAttribute = attributes.get("name");
        if (nameAttribute != null) {
            return nameAttribute.toLowerCase().replace(" ", "-");
        }
        return defaultName;
    }
    
    private Object parseMetricValue(String value) throws NumberFormatException {
        if (value == null) return null;
        
        try {
            return NumberUtils.createLong(value);
        } catch (NumberFormatException e) {
            return NumberUtils.createDouble(value);
        }
    }
    
    private void sendMetricToKafka(String application, String category, String name, Object value, String unit, Map<String, String> attributes) {
        try {
            Map<String, Object> metricData = new HashMap<>();
            metricData.put("timestamp", Instant.now().toEpochMilli());
            metricData.put("application", application);
            metricData.put("category", category);
            metricData.put("name", name);
            metricData.put("value", value);
            metricData.put("unit", unit);
            metricData.put("attributes", attributes);
            
            String jsonPayload = objectMapper.writeValueAsString(metricData);
            String key = String.format("%s.%s.%s", application, category, name);
            
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, jsonPayload);
            
            Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    LOGGER.error("Failed to send metric to Kafka: key={}, payload={}", key, jsonPayload, exception);
                } else {
                    LOGGER.debug("Metric sent to Kafka successfully: topic={}, partition={}, offset={}, key={}", 
                               metadata.topic(), metadata.partition(), metadata.offset(), key);
                }
            });
            
            // Don't wait for the result to avoid blocking, but log if there was an immediate error
            
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize metric data to JSON: application={}, category={}, name={}, value={}", 
                        application, category, name, value, e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error sending metric to Kafka: application={}, category={}, name={}, value={}", 
                        application, category, name, value, e);
        }
    }
    
    public void close() {
        try {
            producer.flush();
            producer.close();
            LOGGER.info("KafkaMetricConsumer closed successfully");
        } catch (Exception e) {
            LOGGER.error("Error closing KafkaMetricConsumer", e);
        }
    }
}