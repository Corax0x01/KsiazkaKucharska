package szymanski.jakub.KsiazkaKucharska.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientKey;

import java.util.List;

@Repository
public interface RecipeIngredientsRepository extends CrudRepository<RecipeIngredientEntity, RecipeIngredientKey> {

    List<RecipeIngredientEntity> findAllByRecipeId(Long recipeId);
    List<RecipeIngredientEntity> findAllByIngredientId(Long ingredientId);

}
