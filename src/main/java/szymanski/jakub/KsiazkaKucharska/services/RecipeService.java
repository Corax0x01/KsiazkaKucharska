package szymanski.jakub.KsiazkaKucharska.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;


import java.util.List;
import java.util.Optional;

public interface RecipeService {

    List<RecipeEntity> findAll();
    Page<RecipeEntity> findAll(Pageable pageable);
    Optional<RecipeEntity> find(Long id);
    RecipeEntity save(RecipeEntity recipeEntity);

    RecipeEntity partialUpdate(Long id, RecipeEntity recipeEntity);
    void delete(Long id);
    void delete(RecipeEntity recipeEntity);
    boolean exists(Long id);

}
