package szymanski.jakub.KsiazkaKucharska.services;

import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;

public interface UserService {

    Iterable<UserEntity> findAllUsers();

    UserEntity findUser(Long id);

    UserEntity findUser(String username);

    UserEntity saveUser(UserEntity userEntity);

    UserEntity updateUser(UserEntity userEntity);

    void deleteUser(Long id);

    void deleteUser(UserEntity userEntity);

}
