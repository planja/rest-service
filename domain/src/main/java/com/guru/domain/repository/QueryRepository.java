package com.guru.domain.repository;

import com.guru.domain.model.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface QueryRepository extends CrudRepository<Query, Long> {
    @Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE Query c SET c.status = :status WHERE c.id = :id")
    @Transactional
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
