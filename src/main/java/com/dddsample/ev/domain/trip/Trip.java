package com.dddsample.ev.domain.trip;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by azhu on 17/09/2017.
 */
@Entity
public class Trip {

    @Id
    private String id;
    private String origination;
    private String destination;

    public String getOrigination() {
        return origination;
    }

    public String getDestination() {
        return destination;
    }
}
