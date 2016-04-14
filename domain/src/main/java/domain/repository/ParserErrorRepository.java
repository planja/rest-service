package domain.repository;

import domain.model.ParserError;
import org.springframework.data.repository.CrudRepository;

public interface ParserErrorRepository extends CrudRepository<ParserError, Long> {
}
