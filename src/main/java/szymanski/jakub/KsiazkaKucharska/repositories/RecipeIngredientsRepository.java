package szymanski.jakub.KsiazkaKucharska.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.KsiazkaKucharska.domain.RecipeIngredient;
import szymanski.jakub.KsiazkaKucharska.domain.RecipeIngredientKey;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredient, RecipeIngredientKey> {
}
