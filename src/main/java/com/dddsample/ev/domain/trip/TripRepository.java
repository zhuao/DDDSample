package com.dddsample.ev.domain.trip;

/**
 * Created by azhu on 17/09/2017.
 */
public interface TripRepository {
    public Trip save(String origination, String destination);
}
