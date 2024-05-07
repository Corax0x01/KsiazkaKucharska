package szymanski.jakub.backend.user.services.impl;

import org.springframework.stereotype.Service;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.shared.mappers.Mapper;
import szymanski.jakub.backend.user.repositories.UserRepository;
import szymanski.jakub.backend.user.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserDto> userMapper;


    public UserServiceImpl(UserRepository userRepository, Mapper<UserEntity, UserDto> userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::mapTo).toList();
    }

    public Optional<UserDto> find(Long id) {
        return userRepository.findById(id).map(userMapper::mapTo);
    }

    public Optional<UserDto> find(String username) {
        return userRepository.findByUsername(username).map(userMapper::mapTo);
    }

    public UserDto save(UserDto user) {
        UserEntity userEntity = userMapper.mapFrom(user);
        return userMapper.mapTo(userRepository.save(userEntity));
    }

    public UserDto partialUpdate(Long id, UserDto user){
        user.setId(id);

        UserEntity userEntity = userMapper.mapFrom(user);

        UserEntity updatedUser = userRepository.findById(id).map(existingUser -> {
           Optional.ofNullable(userEntity.getUsername()).ifPresent(existingUser::setUsername);
           Optional.ofNullable(userEntity.getPassword()).ifPresent(existingUser::setPassword);
           Optional.ofNullable(userEntity.getEmail()).ifPresent(existingUser::setEmail);
           return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.mapTo(updatedUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void delete(UserDto user) {
        UserEntity userEntity = userMapper.mapFrom(user);
        userRepository.delete(userEntity);
    }

    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }
}
