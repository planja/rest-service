package domain.repository;


import domain.model.ParserAnswer;
import domain.model.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParserAnswerRepository extends CrudRepository<ParserAnswer, Long> {
}
