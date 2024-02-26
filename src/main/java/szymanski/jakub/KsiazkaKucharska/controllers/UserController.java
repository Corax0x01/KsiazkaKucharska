package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;
import szymanski.jakub.KsiazkaKucharska.domain.dto.UserDto;
import szymanski.jakub.KsiazkaKucharska.services.UserService;

@Log
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<UserEntity> getUser() {

        return userService.findAllUsers();

    }

    @GetMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserEntity getUserById(@PathVariable Long id) {
        return userService.findUser(id);
    }

    @PostMapping("/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto user) {
        log.info("Creating user: " + user);

        userService.saveUser(user);
    }

    @PutMapping("/users")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateUser(@RequestBody UserEntity userEntity) {
        log.info("Updating user with id: " + userEntity.getId() + " to: " + userEntity);

        userService.updateUser(userEntity);
    }

    @PatchMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateUser(@PathVariable final Long id, @RequestBody UserEntity userEntity) {
        log.info("Updating user with id: " + id + " to: " + userEntity);

        userService.updateUser(id, userEntity);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final Long id) {
        log.info("Deleting user with id: " + id);

        userService.deleteUser(id);
    }
}
