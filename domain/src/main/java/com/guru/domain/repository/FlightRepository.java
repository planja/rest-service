package com.guru.domain.repository;

import com.guru.domain.model.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Никита on 15.04.2016.
 */
@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
}
