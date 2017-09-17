package com.dddsample.ev.application;

import com.dddsample.ev.domain.trip.Trip;
import com.dddsample.ev.domain.trip.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by azhu on 17/09/2017.
 */
public class TripService {

    @Autowired
    TripRepository tripRepository;

    public Trip pubTip(String origination, String destination) {
        return tripRepository.save(origination, destination);
    }
}
