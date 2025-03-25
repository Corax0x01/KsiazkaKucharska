package szymanski.jakub.backend.recipe.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;

import java.util.List;

public interface RecipeService {

    /**
     * Finds all recipes.
     *
     * @return  List of all {@link RecipeEntity} objects
     */
    List<RecipeEntity> findAll();

    /**
     * Finds all recipes with pagination.
     *
     * @param   pageable    pagination information
     * @return              all recipes with pagination
     */
    Page<RecipeEntity> findAllWithPagination(Pageable pageable);

    /**
     * Finds all recipes with given tags.
     *
     * @param   tagsEnumList    list of tags
     * @return                  list of {@link RecipeEntity} objects that match given tags
     */
    List<RecipeEntity> findRecipeByTags(List<TagsEnum> tagsEnumList);

    /**
     * Finds all recipes with given tags with pagination.
     *
     * @param   tagsEnumList    list of tags
     * @param   pageable        pagination information
     * @return                  {@link RecipeEntity} objects that match given tags with pagination
     */
    Page<RecipeEntity> findAllByTags(List<TagsEnum> tagsEnumList, Pageable pageable);

    /**
     * Finds recipes created by logged user with pagination.
     *
     * @param   pageable        pagination information
     * @param   connectedUser   authenticated user
     * @return                  {@link RecipeEntity} objects created by user that is currently logged in
     */
    Page<RecipeEntity> findAllByAuthor(Pageable pageable, Authentication connectedUser);

    /**
     * Finds recipe with given ID.
     *
     * @param   id                      ID of recipe to be found
     * @return                          {@link RecipeEntity} object with given ID
     * @throws RecipeNotFoundException  if recipe with given ID was not found
     */
    RecipeEntity find(Long id);

    /**
     * Creates recipe.
     *
     * @param   request         {@link CreateRecipeRequest} containing data required to create recipe
     * @param   connectedUser   authenticated user
     * @return                  ID of created recipe
     */
    Long create(CreateRecipeRequest request, Authentication connectedUser);

    /**
     * Saves recipe.
     *
     * @param   recipe          {@link RecipeEntity} object with recipe's data to save
     * @return                  ID of saved recipe
     */
    Long save(RecipeEntity recipe);

    /**
     * Updates recipe.
     *
     * @param   id      ID of recipe to be updated
     * @param   recipe  {@link RecipeEntity} object containing recipe's updated data
     * @return          ID of updated recipe
     * @throws  RecipeNotFoundException if recipe was not found
     */
    Long partialUpdate(Long id, RecipeEntity recipe);

    /**
     * Deletes recipe.
     *
     * @param   id  ID of recipe to delete
     */
    void delete(Long id);

    /**
     * Deletes recipe.
     *
     * @param   recipe  {@link RecipeEntity} object to delete
     */
    void delete(RecipeEntity recipe);

    /**
     * Checks if recipe with given ID exists.
     *
     * @param   id              ID of recipe to check
     * @return                  <code>true</code> if recipe exists, <code>false</code> otherwise
     */
    boolean exists(Long id);

}
