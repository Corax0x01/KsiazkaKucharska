package szymanski.jakub.backend.recipe.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;

import java.util.List;

public interface RecipeService {

    List<RecipeEntity> findAll();
    Page<RecipeEntity> findAllWithPagination(Pageable pageable);
    List<RecipeEntity> findRecipeByTags(List<TagsEnum> tagsEnumList);
    Page<RecipeEntity> findAllByTags(List<TagsEnum> tagsEnumList, Pageable pageable);
    Page<RecipeEntity> findAllByAuthor(Pageable pageable, Authentication connectedUser);
    RecipeEntity find(Long id);
    Long create(CreateRecipeRequest request, Authentication connectedUser);
    Long save(RecipeEntity recipe);
    Long partialUpdate(Long id, RecipeEntity recipe);
    void delete(Long id);
    void delete(RecipeEntity recipe);
    boolean exists(Long id);

}
