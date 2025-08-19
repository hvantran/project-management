package com.hoatv.metric.mgmt.entities;

import java.util.Map;

public class MetricTag {
    private String value;
    private Map<String, String> attributes;
    
    public MetricTag(String value, Map<String, String> attributes) {
        this.value = value;
        this.attributes = attributes;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}