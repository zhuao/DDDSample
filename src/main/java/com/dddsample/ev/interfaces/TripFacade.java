package com.dddsample.ev.interfaces;

import com.dddsample.ev.application.TripService;
import com.dddsample.ev.domain.trip.Trip;
import com.dddsample.ev.interfaces.assembler.TripAssembler;
import io.swagger.api.TripsApiDelegate;
import io.swagger.model.TripResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by azhu on 17/09/2017.
 */
public class TripFacade implements TripsApiDelegate {

    @Autowired
    TripService tripService;

    private TripAssembler tripAssembler = new TripAssembler();

    @Override
    public ResponseEntity<TripResponse> pubTrips(String origination, String destination) {
        Trip pubedTrip = tripService.pubTip(origination, destination);
        return new ResponseEntity<TripResponse>(tripAssembler.toDTO(pubedTrip), HttpStatus.CREATED);
    }
}
