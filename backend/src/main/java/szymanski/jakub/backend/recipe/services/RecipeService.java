package szymanski.jakub.backend.recipe.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    List<RecipeDto> findAll();
    Page<RecipeDto> findAllWithPagination(Pageable pageable);
    List<RecipeDto> findRecipeByTags(List<TagsEnum> tagsEnumList);
    Page<RecipeDto> findRecipeByTagsWithPagination(List<TagsEnum> tagsEnumList, Pageable pageable);
    Optional<RecipeDto> find(Long id);
    RecipeDto create(RecipeDto recipe, JsonNode ingredients);
    RecipeDto save(RecipeDto recipe);
    RecipeDto partialUpdate(Long id, RecipeDto recipe);
    void delete(Long id);
    void delete(RecipeDto recipe);
    boolean exists(Long id);

}
