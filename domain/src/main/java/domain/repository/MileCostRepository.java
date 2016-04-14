package domain.repository;


import domain.model.MileCost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MileCostRepository extends CrudRepository<MileCost, Integer> {

    //MileCost findById(Integer id);
}