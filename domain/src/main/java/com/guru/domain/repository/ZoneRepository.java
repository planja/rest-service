package com.guru.domain.repository;

import com.guru.domain.model.Zone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Никита on 18.04.2016.
 */
@Repository
public interface ZoneRepository extends CrudRepository<Zone, Long> {
}
