package szymanski.jakub.backend.recipe.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Page<RecipeEntity> findAllByUserEntity(Pageable pageable, UserEntity author);
}
