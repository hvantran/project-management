# Kafka Metric Consumer

This module provides a `KafkaMetricConsumer` implementation that sends metric information to Apache Kafka topics for real-time processing and analytics.

## Overview

The `KafkaMetricConsumer` implements the `MetricConsumerHandler` interface and is annotated with `@MetricConsumer` to integrate with the existing metric management system. It converts various types of metric data into JSON format and sends them to a configured Kafka topic.

## Features

- **Multiple Data Types**: Supports SimpleValue, ComplexValue, primitives (String, Long, Integer, Double), and collections
- **JSON Serialization**: Automatically converts metrics to structured JSON format
- **Configurable**: Supports custom Kafka broker settings, topic names, and producer configurations
- **Error Handling**: Robust error handling with proper logging
- **Asynchronous**: Non-blocking message sending to Kafka

## Dependencies

- Apache Kafka Clients 3.7.0
- Jackson for JSON serialization
- SLF4J for logging
- Apache Commons Lang3 for utilities

## Quick Start

### Basic Usage

```java
// Default configuration (localhost:9092, topic: "metrics")
KafkaMetricConsumer consumer = new KafkaMetricConsumer();

// Send a simple metric
consumer.consume("my-app", "performance", "cpu-usage", 75.5, "percent");

// Clean up
consumer.close();
```

### Custom Configuration

```java
KafkaMetricConfig config = new KafkaMetricConfig();
config.setBootstrapServers("kafka-cluster:9092");
config.setTopicName("application-metrics");
config.setRetries(5);

KafkaMetricConsumer consumer = new KafkaMetricConsumer(config);
```

### Complex Metrics

```java
// Metric with attributes
Map<String, String> attributes = new HashMap<>();
attributes.put("host", "web-server-01");
attributes.put("region", "us-east-1");

MetricTag tag = new MetricTag("85.2", attributes);
ComplexValue complexValue = new ComplexValue(Collections.singletonList(tag));

consumer.consume("my-app", "infrastructure", "server-cpu", complexValue, "percent");
```

## Message Format

Messages sent to Kafka have the following JSON structure:

```json
{
  "timestamp": 1692454800000,
  "application": "my-app",
  "category": "performance",
  "name": "cpu-usage",
  "value": 75.5,
  "unit": "percent",
  "attributes": {
    "host": "server1",
    "region": "us-east-1"
  }
}
```

## Configuration Options

| Property | Default | Description |
|----------|---------|-------------|
| `bootstrapServers` | `localhost:9092` | Kafka broker addresses |
| `topicName` | `metrics` | Target topic name |
| `acks` | `1` | Acknowledgment level |
| `retries` | `3` | Number of retries |
| `batchSize` | `16384` | Batch size for producer |
| `lingerMs` | `5` | Linger time in milliseconds |

## Building

```bash
mvn clean package
```

## Testing

```bash
mvn test
```

The tests validate the metric processing logic without requiring an actual Kafka cluster.

## Integration

To integrate with the existing metric management system:

1. Add the `kafka-metric-consumer` as a dependency
2. Register the `KafkaMetricConsumer` with your `MetricConsumerRegistry`
3. Configure with appropriate Kafka settings for your environment

```java
MetricConsumerRegistry registry = new MetricConsumerRegistry();
KafkaMetricConsumer kafkaConsumer = new KafkaMetricConsumer("prod-kafka:9092", "prod-metrics");
registry.add(kafkaConsumer);
```