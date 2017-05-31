package com.tw.practise.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Created by azhu on 25/05/2017.
 */
@Entity
public class Instance {
    public static final String DEFAULT_REGION = "SZ";

    public String getInstanceId() {
        return instanceId;
    }

    @Id
    private String instanceId;
    private String name;
    private String imageId;
    private String flavorId;
    @Transient
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
