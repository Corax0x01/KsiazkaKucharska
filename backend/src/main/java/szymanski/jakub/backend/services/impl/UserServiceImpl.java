package szymanski.jakub.backend.services.impl;

import org.springframework.stereotype.Service;
import szymanski.jakub.backend.domain.entities.UserEntity;
import szymanski.jakub.backend.repositories.UserRepository;
import szymanski.jakub.backend.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> findAll() {
        return (List<UserEntity>) userRepository.findAll();
    }

    public Optional<UserEntity> find(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserEntity> find(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity partialUpdate(Long id, UserEntity userEntity){
        userEntity.setId(id);

        return userRepository.findById(id).map(existingUser -> {
           Optional.ofNullable(userEntity.getUsername()).ifPresent(existingUser::setUsername);
           Optional.ofNullable(userEntity.getPassword()).ifPresent(existingUser::setPassword);
           Optional.ofNullable(userEntity.getEmail()).ifPresent(existingUser::setEmail);
           return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }

    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }
}
