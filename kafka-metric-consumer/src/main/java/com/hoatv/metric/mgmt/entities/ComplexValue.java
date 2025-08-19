package com.hoatv.metric.mgmt.entities;

import java.util.Collection;

public class ComplexValue {
    private Collection<MetricTag> tags;
    
    public ComplexValue(Collection<MetricTag> tags) {
        this.tags = tags;
    }
    
    public Collection<MetricTag> getTags() {
        return tags;
    }
    
    public void setTags(Collection<MetricTag> tags) {
        this.tags = tags;
    }
}