package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.User;
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
    public Iterable<User> getUser() {

        return userService.findAllUsers();

    }

    @GetMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public User getUserById(@PathVariable Long id) {
        return userService.findUser(id);
    }

    @PostMapping("/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createUser(@RequestBody final User user) {
        log.info("Creating user: " + user);

        userService.saveUser(user);
    }

    @PutMapping("/users")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateUser(@RequestBody final User user) {
        log.info("Updating user with id: " + user.getId() + " to: " + user);

        userService.updateUser(user);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final Long id) {
        log.info("Deleting user with id: " + id);

        userService.deleteUser(id);
    }
}
