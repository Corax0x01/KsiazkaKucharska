package szymanski.jakub.backend.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.services.UserService;

import java.util.List;
import java.util.Optional;

@RequestMapping("users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable("id") Long id) {

        UserDto user = userService.find(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Long> createUser(
            @RequestBody UserDto user) {

        Long savedUserId = userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> fullUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto user) {

        Long updatedUserId = userService.save(user);

        return ResponseEntity.ok(updatedUserId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto user) {

        Long updatedUserId = userService.partialUpdate(id, user);
        return ResponseEntity.ok(updatedUserId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
