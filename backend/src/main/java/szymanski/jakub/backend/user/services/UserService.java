package szymanski.jakub.backend.user.services;

import szymanski.jakub.backend.user.dtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();

    UserDto find(Long id);

    UserDto find(String username);

    Long save(UserDto user);

    Long partialUpdate(Long id, UserDto user);

    void delete(Long id);

    void delete(UserDto user);

    boolean exists(Long id);

}
