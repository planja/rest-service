package com.guru.domain.repository;

import com.guru.domain.model.MileCost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.inject.Named;
import java.util.List;

@Repository
public interface MileCostRepository extends CrudRepository<MileCost, Long> {

}
