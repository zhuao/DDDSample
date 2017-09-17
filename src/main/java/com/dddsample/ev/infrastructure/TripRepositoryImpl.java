package com.dddsample.ev.infrastructure;

import com.dddsample.ev.domain.trip.Trip;
import com.dddsample.ev.domain.trip.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

/**
 * Created by azhu on 17/09/2017.
 */
public class TripRepositoryImpl extends SimpleJpaRepository<Trip, String> implements TripRepository {

    public TripRepositoryImpl(@Autowired EntityManager em) {
        super(Trip.class, em);
    }

    @Override
    public Trip save(String origination, String destination) {
        return null;
    }
}
