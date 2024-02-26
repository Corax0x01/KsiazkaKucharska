package szymanski.jakub.KsiazkaKucharska.services;

import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> findAll();

    Optional<UserEntity> find(Long id);

    Optional<UserEntity> find(String username);

    UserEntity save(UserEntity userEntity);

    UserEntity partialUpdate(Long id, UserEntity userEntity);

    void delete(Long id);

    void delete(UserEntity userEntity);

    boolean exists(Long id);

}
