package com.hoatv.metric.mgmt.config;

/**
 * Configuration class for Kafka metric consumer settings.
 * This class holds the configuration parameters needed to initialize a KafkaMetricConsumer.
 */
public class KafkaMetricConfig {
    
    private String bootstrapServers = "localhost:9092";
    private String topicName = "metrics";
    private String acks = "1";
    private int retries = 3;
    private int batchSize = 16384;
    private int lingerMs = 5;
    
    public KafkaMetricConfig() {
    }
    
    public KafkaMetricConfig(String bootstrapServers, String topicName) {
        this.bootstrapServers = bootstrapServers;
        this.topicName = topicName;
    }
    
    public String getBootstrapServers() {
        return bootstrapServers;
    }
    
    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }
    
    public String getTopicName() {
        return topicName;
    }
    
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
    
    public String getAcks() {
        return acks;
    }
    
    public void setAcks(String acks) {
        this.acks = acks;
    }
    
    public int getRetries() {
        return retries;
    }
    
    public void setRetries(int retries) {
        this.retries = retries;
    }
    
    public int getBatchSize() {
        return batchSize;
    }
    
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    
    public int getLingerMs() {
        return lingerMs;
    }
    
    public void setLingerMs(int lingerMs) {
        this.lingerMs = lingerMs;
    }
}