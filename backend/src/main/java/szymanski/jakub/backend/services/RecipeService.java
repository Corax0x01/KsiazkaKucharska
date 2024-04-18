package szymanski.jakub.backend.services;

import szymanski.jakub.backend.domain.TagsEnum;
import szymanski.jakub.backend.domain.dto.RecipeDto;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    List<RecipeDto> findAll();
    List<RecipeDto> findRecipeByTags(List<TagsEnum> tagsEnumList);
    Optional<RecipeDto> find(Long id);
    RecipeDto save(RecipeDto recipeE);

    RecipeDto partialUpdate(Long id, RecipeDto recipe);
    void delete(Long id);
    void delete(RecipeDto recipe);
    boolean exists(Long id);

}
