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

    /**
     * Fetches all users.
     *
     * @return  {@link ResponseEntity} containing list of all users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {

        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Fetches user with given ID.
     *
     * @param   id  ID of the user
     * @return      {@link ResponseEntity} containing {@link UserDto} object with given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable("id") Long id) {

        UserDto user = userService.find(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Creates a user.
     *
     * @param   user    {@link UserDto} object that contains data for creating user
     * @return          {@link ResponseEntity} containing ID of created user
     */
    @PostMapping
    public ResponseEntity<Long> createUser(
            @RequestBody UserDto user) {

        Long savedUserId = userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserId);
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<Long> fullUpdateUser(
//            @PathVariable("id") Long id,
//            @RequestBody UserDto user) {
//
//        user.setId(id);
//        Long updatedUserId = userService.save(user);
//
//        return ResponseEntity.ok(updatedUserId);
//    }

    /**
     * Updates user with given ID.
     *
     * @param   id      ID of the user to update
     * @param   user    {@link UserDto} object that contains data for updating user
     * @return          {@link ResponseEntity} containing ID of updated user
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto user) {

        Long updatedUserId = userService.partialUpdate(id, user);
        return ResponseEntity.ok(updatedUserId);
    }

    /**
     * Deletes user with given ID.
     *
     * @param   id  ID of the user to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
