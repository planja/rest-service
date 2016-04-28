package com.guru.domain.repository;

import com.guru.domain.model.Trip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.inject.Named;

@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {
}
