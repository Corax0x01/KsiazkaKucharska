package szymanski.jakub.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.domain.entities.RecipeEntity;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
}
