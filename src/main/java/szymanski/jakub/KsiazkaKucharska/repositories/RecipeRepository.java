package szymanski.jakub.KsiazkaKucharska.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.KsiazkaKucharska.domain.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
