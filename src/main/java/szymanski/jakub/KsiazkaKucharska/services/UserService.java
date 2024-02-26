package szymanski.jakub.KsiazkaKucharska.services;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;
import szymanski.jakub.KsiazkaKucharska.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity findUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity findUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public void updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUser(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }
}
