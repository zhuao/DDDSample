package com.tw.practise.domain;

import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

/**
 * Created by azhu on 25/05/2017.
 */
public class Instance {
    public static final String DEFAULT_REGION = "SZ";

    public String getInstanceId() {
        return instanceId;
    }

    private String instanceId;
    private String name;
    private String imageId;
    private String flavorId;
    private Region region;
    private String az;

    public Instance() {
        region = new Region(DEFAULT_REGION);
    }

    public Instance(String regionName) {
        region = new Region(regionName);
    }

    public Instance(String name, String imageId, String flavorId) {
        this.name = name;
        this.imageId = imageId;
        this.flavorId = flavorId;
    }

    public String region() {
        return region.name();
    }

    public void setAZ(String AZ) {
        this.az = AZ;
    }

    public String getAZ() {
        return az;
    }

    public String getFlavorId() {
        return flavorId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
