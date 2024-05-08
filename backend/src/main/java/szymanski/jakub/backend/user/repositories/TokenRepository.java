package szymanski.jakub.backend.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import szymanski.jakub.backend.user.entities.TokenEntity;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String token);
}
