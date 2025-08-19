package com.hoatv.metric.mgmt.entities;

public class SimpleValue {
    private Object value;
    
    public SimpleValue(Object value) {
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
}