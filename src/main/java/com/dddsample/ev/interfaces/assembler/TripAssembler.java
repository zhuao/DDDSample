package com.dddsample.ev.interfaces.assembler;

import com.dddsample.ev.domain.trip.Trip;
import io.swagger.model.TripResponse;

/**
 * Created by azhu on 17/09/2017.
 */
public class TripAssembler {
    public TripResponse toDTO(Trip trip) {
        return new TripResponse()
                .origination(trip.getOrigination())
                .destination(trip.getDestination());
    }
}
