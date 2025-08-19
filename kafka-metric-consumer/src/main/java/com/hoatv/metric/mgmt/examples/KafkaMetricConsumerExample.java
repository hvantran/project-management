package com.hoatv.metric.mgmt.examples;

import com.hoatv.metric.mgmt.config.KafkaMetricConfig;
import com.hoatv.metric.mgmt.consumers.KafkaMetricConsumer;
import com.hoatv.metric.mgmt.entities.ComplexValue;
import com.hoatv.metric.mgmt.entities.MetricTag;
import com.hoatv.metric.mgmt.entities.SimpleValue;

import java.util.*;

/**
 * Example usage of KafkaMetricConsumer to demonstrate how to send various types of metrics to Kafka.
 */
public class KafkaMetricConsumerExample {
    
    public static void main(String[] args) {
        // Example 1: Using default configuration
        KafkaMetricConsumer defaultConsumer = new KafkaMetricConsumer();
        
        // Example 2: Using custom configuration
        KafkaMetricConfig config = new KafkaMetricConfig();
        config.setBootstrapServers("kafka-cluster:9092");
        config.setTopicName("application-metrics");
        config.setRetries(5);
        
        KafkaMetricConsumer customConsumer = new KafkaMetricConsumer(config);
        
        // Example metrics
        sendSimpleMetrics(customConsumer);
        sendComplexMetrics(customConsumer);
        
        // Clean up
        defaultConsumer.close();
        customConsumer.close();
    }
    
    private static void sendSimpleMetrics(KafkaMetricConsumer consumer) {
        // Simple numeric metrics
        consumer.consume("action-manager", "performance", "cpu-usage", 75.5, "percent");
        consumer.consume("action-manager", "performance", "memory-usage", 1024L, "MB");
        consumer.consume("action-manager", "performance", "active-connections", 42, "count");
        
        // SimpleValue object
        SimpleValue responseTime = new SimpleValue(250.0);
        consumer.consume("action-manager", "performance", "avg-response-time", responseTime, "ms");
    }
    
    private static void sendComplexMetrics(KafkaMetricConsumer consumer) {
        // Complex metric with attributes
        Map<String, String> serverAttributes = new HashMap<>();
        serverAttributes.put("host", "web-server-01");
        serverAttributes.put("region", "us-east-1");
        serverAttributes.put("instance-type", "m5.large");
        
        MetricTag serverMetric = new MetricTag("85.2", serverAttributes);
        ComplexValue serverComplexValue = new ComplexValue(Collections.singletonList(serverMetric));
        
        consumer.consume("action-manager", "infrastructure", "server-cpu-utilization", serverComplexValue, "percent");
        
        // Multiple complex metrics
        List<ComplexValue> instanceMetrics = new ArrayList<>();
        
        // Instance 1
        Map<String, String> instance1Attrs = Map.of("instance-id", "i-123456", "availability-zone", "us-east-1a");
        MetricTag instance1Tag = new MetricTag("70.0", instance1Attrs);
        instanceMetrics.add(new ComplexValue(Collections.singletonList(instance1Tag)));
        
        // Instance 2
        Map<String, String> instance2Attrs = Map.of("instance-id", "i-789012", "availability-zone", "us-east-1b");
        MetricTag instance2Tag = new MetricTag("65.5", instance2Attrs);
        instanceMetrics.add(new ComplexValue(Collections.singletonList(instance2Tag)));
        
        consumer.consume("action-manager", "infrastructure", "instance-cpu-metrics", instanceMetrics, "percent");
        
        // Metric with custom unit in attributes
        Map<String, String> responseTimeAttrs = new HashMap<>();
        responseTimeAttrs.put("unit", "milliseconds");
        responseTimeAttrs.put("endpoint", "/api/actions");
        responseTimeAttrs.put("method", "GET");
        
        MetricTag responseTimeTag = new MetricTag("150", responseTimeAttrs);
        ComplexValue responseTimeComplex = new ComplexValue(Collections.singletonList(responseTimeTag));
        
        consumer.consume("action-manager", "api", "endpoint-response-time", responseTimeComplex, "seconds");
    }
}