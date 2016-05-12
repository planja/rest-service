package com.guru.domain.repository;

import com.guru.domain.model.Airport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Anton on 05.05.2016.
 */
@Repository
public interface AirportRepository extends CrudRepository<Airport, Long> {
}
