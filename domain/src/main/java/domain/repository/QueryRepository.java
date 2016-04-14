package domain.repository;

import domain.model.MileCost;
import domain.model.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryRepository extends CrudRepository<Query, Long> {

}
