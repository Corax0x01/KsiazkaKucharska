package szymanski.jakub.KsiazkaKucharska.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
}
