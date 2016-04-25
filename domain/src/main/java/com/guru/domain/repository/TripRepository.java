package com.guru.domain.repository;

import com.guru.domain.model.Trip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Никита on 15.04.2016.
 */
@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {
}
