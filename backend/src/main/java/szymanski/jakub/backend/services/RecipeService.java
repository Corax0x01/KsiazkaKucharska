package szymanski.jakub.backend.services;

import szymanski.jakub.backend.domain.entities.RecipeEntity;


import java.util.List;
import java.util.Optional;

public interface RecipeService {

    List<RecipeEntity> findAll();
    Optional<RecipeEntity> find(Long id);
    RecipeEntity save(RecipeEntity recipeEntity);

    RecipeEntity partialUpdate(Long id, RecipeEntity recipeEntity);
    void delete(Long id);
    void delete(RecipeEntity recipeEntity);
    boolean exists(Long id);

}
