package szymanski.jakub.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.domain.dto.UserDto;
import szymanski.jakub.backend.services.UserService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/users")
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

        Optional<UserDto> user = userService.find(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto user) {

        UserDto savedUser = userService.save(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> fullUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto user) {

        if(!userService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        UserDto updatedUser = userService.save(user);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto user) {

        if(!userService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        UserDto updatedUser = userService.partialUpdate(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
