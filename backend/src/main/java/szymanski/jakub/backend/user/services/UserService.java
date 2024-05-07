package szymanski.jakub.backend.user.services;

import szymanski.jakub.backend.user.dtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();

    Optional<UserDto> find(Long id);

    Optional<UserDto> find(String username);

    UserDto save(UserDto user);

    UserDto partialUpdate(Long id, UserDto user);

    void delete(Long id);

    void delete(UserDto user);

    boolean exists(Long id);

}
