package com.guru.domain.repository;

import com.guru.domain.model.Airport;
import com.guru.domain.model.Flight;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Anton on 05.05.2016.
 */
public interface AirportRepository  extends CrudRepository<Airport, Long> {
}
