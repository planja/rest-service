package com.guru.domain.repository;


import com.guru.domain.model.ParserAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParserAnswerRepository extends CrudRepository<ParserAnswer, Long> {
}
