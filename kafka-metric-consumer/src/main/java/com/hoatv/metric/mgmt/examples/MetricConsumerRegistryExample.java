package com.hoatv.metric.mgmt.examples;

import com.hoatv.metric.mgmt.consumers.KafkaMetricConsumer;
import com.hoatv.metric.mgmt.consumers.MetricConsumerHandler;
import com.hoatv.metric.mgmt.services.MetricConsumerRegistry;

/**
 * Example showing how to register and use multiple metric consumers including the new KafkaMetricConsumer.
 */
public class MetricConsumerRegistryExample {
    
    public static void main(String[] args) {
        // Create registry
        MetricConsumerRegistry registry = new MetricConsumerRegistry();
        
        // Add Kafka consumer to registry
        KafkaMetricConsumer kafkaConsumer = new KafkaMetricConsumer("localhost:9092", "app-metrics");
        registry.add(kafkaConsumer);
        
        // Simulate processing metrics through all registered consumers
        String application = "action-manager";
        String category = "performance";
        String metricName = "request-count";
        Long metricValue = 150L;
        String unit = "count";
        
        // Send metric to all consumers in registry
        for (MetricConsumerHandler consumer : registry) {
            consumer.consume(application, category, metricName, metricValue, unit);
        }
        
        System.out.println("Metric sent to " + registry.size() + " consumer(s)");
        
        // Clean up
        kafkaConsumer.close();
    }
}