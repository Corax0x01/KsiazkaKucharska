package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.User;
import szymanski.jakub.KsiazkaKucharska.services.UserService;

import java.util.List;

@Log
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<User> getUser(@RequestParam(name = "id", required = false) final Long id) {

        if(id == null) return userService.findAllUsers();
        else return List.of(userService.findUser(id));

    }

    @PostMapping("/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createUser(@RequestBody final User user) {
        log.info("Creating user: " + user.toString());

        userService.saveUser(user);
    }

    @PutMapping("/users")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateUser(@RequestBody final User user) {
        log.info("Updating user with id: " + user.getId() + " to: " + user.toString());

        userService.updateUser(user);
    }

    @DeleteMapping("/users")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestParam(name = "id") final Long id) {
        log.info("Deleting user with id: " + id);

        userService.deleteUser(id);
    }
}
