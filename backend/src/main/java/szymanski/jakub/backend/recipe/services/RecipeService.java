package szymanski.jakub.backend.recipe.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;

import java.util.List;

public interface RecipeService {

    /**
     * Finds all recipes.
     *
     * @return  list of all {@link RecipeDto} objects
     */
    List<RecipeDto> findAll();

    /**
     * Finds all recipes with pagination.
     *
     * @param   pageable   pagination information
     * @return             list of all {@link RecipeDto} objects with pagination
     */
    Page<RecipeDto> findAllWithPagination(Pageable pageable);

    /**
     * Finds all recipes with given tags.
     *
     * @param   tagsEnumList    list of tags
     * @return                  list of {@link RecipeDto} objects that match given tags
     */
    List<RecipeDto> findRecipeByTags(List<TagsEnum> tagsEnumList);

    /**
     * Finds all recipes with given tags with pagination.
     *
     * @param   tagsEnumList    list of tags
     * @param   pageable        pagination information
     * @return                  {@link RecipeDto} objects that match given tags with pagination
     */
    Page<RecipeDto> findAllByTags(List<TagsEnum> tagsEnumList, Pageable pageable);

    /**
     * Finds all recipes created by user that is currently logged in with pagination.
     *
     * @param   pageable                pagination information
     * @param   auth                    information about authenticated user
     * @return                          {@link RecipeDto} objects created by user that is currently logged in
     * @throws  UserNotFoundException   if authenticated user was not found
     */
    Page<RecipeDto> findAllByAuthor(Pageable pageable, Authentication auth);

    /**
     * Finds recipes created by given user with pagination.
     *
     * @param   pageable                pagination information
     * @param   id                      ID of author
     * @return                          {@link RecipeDto} objects created by user that is currently logged in
     * @throws  UserNotFoundException   if user with given ID was not found
     */
    Page<RecipeDto> findAllByAuthor(Pageable pageable, Long id);

    /**
     * Finds recipe with given ID.
     *
     * @param   id                          ID of recipe to be found
     * @return                              {@link RecipeDto} object with given ID
     * @throws  RecipeNotFoundException     if recipe with given ID was not found
     */
    RecipeDto find(Long id);

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
     * @param   recipe          {@link RecipeDto} object with recipe's data to save
     * @return                  ID of saved recipe
     */
    Long save(RecipeDto recipe);

    /**
     * Updates recipe.
     *
     * @param   id                          ID of recipe to be updated
     * @param   recipe                      {@link RecipeDto} object containing recipe's updated data
     * @return                              ID of updated recipe
     * @throws  RecipeNotFoundException     if recipe was not found
     */
    Long partialUpdate(Long id, RecipeDto recipe);

    /**
     * Deletes recipe.
     *
     * @param   id                          ID of recipe to delete
     * @throws  RecipeNotFoundException     if recipe with given ID was not found
     */
    void delete(Long id);

    /**
     * Deletes recipe.
     *
     * @param   recipe                      {@link RecipeDto} object to delete
     * @throws  RecipeNotFoundException     if recipe with given name was not found
     */
    void delete(RecipeDto recipe);

    /**
     * Checks if recipe with given ID exists.
     *
     * @param   id      ID of recipe to check
     * @return          <code>true</code> if recipe exists, <code>false</code> otherwise
     */
    boolean exists(Long id);

    /**
     * Checks if recipe exists.
     *
     * @param   recipe  {@link RecipeEntity} object to check
     * @return          <code>true</code> if recipe exists, <code>false</code> otherwise
     */
    boolean exists(RecipeEntity recipe);
}
