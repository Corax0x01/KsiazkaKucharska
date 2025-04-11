package szymanski.jakub.backend.recipe.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    /**
     * Finds all recipes created by given user.
     *
     * @param   pageable    pagination information
     * @param   author      user to filter by
     * @return              all {@link RecipeEntity} objects created by given user with pagination
     */
    Page<RecipeEntity> findAllByUserEntity(Pageable pageable, UserEntity author);

    /**
     * Checks if recipe exists.
     *
     * @param   recipe  recipe to check
     * @return          <code>true</code> if recipe exists, <code>false</code> otherwise
     */
    boolean exists(RecipeEntity recipe);
}
