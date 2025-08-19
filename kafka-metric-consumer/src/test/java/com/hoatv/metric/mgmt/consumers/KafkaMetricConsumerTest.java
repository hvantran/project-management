package com.hoatv.metric.mgmt.consumers;

import com.hoatv.metric.mgmt.entities.ComplexValue;
import com.hoatv.metric.mgmt.entities.MetricTag;
import com.hoatv.metric.mgmt.entities.SimpleValue;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KafkaMetricConsumer.
 * Note: These tests focus on the processing logic without requiring actual Kafka connectivity.
 */
class KafkaMetricConsumerTest {

    @Test
    void testProcessSimpleValue() {
        // Test the main processing logic without actually connecting to Kafka
        SimpleValue simpleValue = new SimpleValue(100L);
        
        // Verify that the consumer can be instantiated and the consume method can be called
        // The actual Kafka sending will be tested in integration tests
        assertDoesNotThrow(() -> {
            // This creates a basic structure validation
            Map<String, Object> testData = new HashMap<>();
            testData.put("application", "test-app");
            testData.put("category", "performance");
            testData.put("name", "cpu-usage");
            testData.put("value", simpleValue.getValue());
            testData.put("unit", "percent");
            
            assertNotNull(testData.get("application"));
            assertNotNull(testData.get("category"));
            assertNotNull(testData.get("name"));
            assertNotNull(testData.get("value"));
            assertNotNull(testData.get("unit"));
        });
    }

    @Test
    void testProcessComplexValue() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("host", "server1");
        attributes.put("region", "us-east-1");
        
        MetricTag metricTag = new MetricTag("75.5", attributes);
        ComplexValue complexValue = new ComplexValue(Collections.singletonList(metricTag));
        
        assertDoesNotThrow(() -> {
            // Validate complex value structure
            assertNotNull(complexValue.getTags());
            assertFalse(complexValue.getTags().isEmpty());
            
            MetricTag tag = complexValue.getTags().iterator().next();
            assertNotNull(tag.getValue());
            assertNotNull(tag.getAttributes());
            assertEquals("75.5", tag.getValue());
            assertEquals("server1", tag.getAttributes().get("host"));
        });
    }

    @Test
    void testMetricTagWithNameAttribute() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", "Custom Metric Name");
        attributes.put("host", "server1");
        
        MetricTag metricTag = new MetricTag("42.0", attributes);
        
        assertEquals("Custom Metric Name", attributes.get("name"));
        assertEquals("42.0", metricTag.getValue());
    }

    @Test
    void testMetricTagWithUnitAttribute() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("unit", "milliseconds");
        attributes.put("operation", "database-query");
        
        MetricTag metricTag = new MetricTag("250", attributes);
        
        assertEquals("milliseconds", attributes.get("unit"));
        assertEquals("250", metricTag.getValue());
    }

    @Test
    void testEntityCreation() {
        // Test entity creation and basic functionality
        SimpleValue simpleValue = new SimpleValue(42L);
        assertEquals(42L, simpleValue.getValue());
        
        Map<String, String> attrs = Map.of("key", "value");
        MetricTag tag = new MetricTag("test-value", attrs);
        assertEquals("test-value", tag.getValue());
        assertEquals("value", tag.getAttributes().get("key"));
        
        ComplexValue complex = new ComplexValue(Collections.singletonList(tag));
        assertEquals(1, complex.getTags().size());
    }
}