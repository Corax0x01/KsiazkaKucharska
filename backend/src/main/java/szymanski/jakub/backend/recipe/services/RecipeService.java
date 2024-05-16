package szymanski.jakub.backend.recipe.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.requests.CreateRecipeRequest;

import java.util.List;

public interface RecipeService {

    List<RecipeDto> findAll();
    Page<RecipeDto> findAllWithPagination(Pageable pageable);
    List<RecipeDto> findRecipeByTags(List<TagsEnum> tagsEnumList);
    Page<RecipeDto> findAllByTags(List<TagsEnum> tagsEnumList, Pageable pageable);
    Page<RecipeDto> findAllByAuthor(Pageable pageable, Authentication connectedUser);
    RecipeDto find(Long id);
    Long create(CreateRecipeRequest request, Authentication connectedUser);
    Long save(RecipeDto recipe);
    Long partialUpdate(Long id, RecipeDto recipe);
    void delete(Long id);
    void delete(RecipeDto recipe);
    boolean exists(Long id);

}
