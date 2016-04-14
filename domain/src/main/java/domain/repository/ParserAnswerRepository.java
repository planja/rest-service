package domain.repository;


import domain.model.ParserAnswer;
import domain.model.Query;
import org.springframework.data.repository.CrudRepository;

public interface ParserAnswerRepository extends CrudRepository<ParserAnswer, Long> {
}
