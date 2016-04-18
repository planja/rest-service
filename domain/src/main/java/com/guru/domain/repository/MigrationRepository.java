package com.guru.domain.repository;

import com.guru.domain.model.Migration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Никита on 18.04.2016.
 */
@Repository
public interface MigrationRepository extends CrudRepository<Migration,Long> {
}
