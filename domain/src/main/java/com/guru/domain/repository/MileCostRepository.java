package com.guru.domain.repository;

import com.guru.domain.model.MileCost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Никита on 06.05.2016.
 */
@Repository
public interface MileCostRepository extends CrudRepository<MileCost, Long> {
}
