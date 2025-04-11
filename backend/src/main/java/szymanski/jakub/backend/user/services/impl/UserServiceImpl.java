package szymanski.jakub.backend.user.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;
import szymanski.jakub.backend.user.repositories.UserRepository;
import szymanski.jakub.backend.user.services.UserService;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserDto> userMapper;


    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::mapTo).toList();
    }

    public UserDto find(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapTo)
                .orElseThrow(
                        () -> new UserNotFoundException("User with ID: " + id + " not found")
                );
    }

    public UserDto find(String username) {
        return userRepository.findByUsername(username).map(userMapper::mapTo)
                .orElseThrow(
                        () -> new UserNotFoundException("User with username: " + username + " not found")
                );
    }

    public Long save(UserDto user) {
        UserEntity userEntity = userMapper.mapFrom(user);
        return userRepository.save(userEntity).getId();
    }

    public Long partialUpdate(Long id, UserDto user){
        user.setId(id);

        UserEntity userEntity = userMapper.mapFrom(user);

        UserEntity updatedUser = userRepository.findById(id).map(existingUser -> {
           Optional.ofNullable(userEntity.getUsername()).ifPresent(existingUser::setUsername);
           Optional.ofNullable(userEntity.getPassword()).ifPresent(existingUser::setPassword);
           Optional.ofNullable(userEntity.getEmail()).ifPresent(existingUser::setEmail);
           return userRepository.save(existingUser);
        }).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        return updatedUser.getId();
    }

    public void delete(Long id) {
        if(!exists(id)) {
            throw new UserNotFoundException("User with ID: " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public void delete(UserDto user) {
        UserEntity userEntity = userMapper.mapFrom(user);
        if(!exists(userEntity)) {
            throw new UserNotFoundException("User " + userEntity.toString() + " not found");
        }
        userRepository.delete(userEntity);
    }

    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    public boolean exists(UserEntity user) {

        return userRepository.exists(user);
    }
}
