package com.guru.domain.repository;

import com.guru.domain.model.ParserError;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParserErrorRepository extends CrudRepository<ParserError, Long> {
}
