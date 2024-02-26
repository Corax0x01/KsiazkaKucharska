package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;
import szymanski.jakub.KsiazkaKucharska.domain.dto.UserDto;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;
import szymanski.jakub.KsiazkaKucharska.services.impl.UserServiceImpl;

import java.util.List;

@Log
@RestController
public class UserController {

    private final UserServiceImpl userService;

    private final Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserServiceImpl userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getUser() {

        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        return new ResponseEntity<>(userMapper.mapTo(userService.findUser(id)), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        log.info("Creating user: " + user);

        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity savedUserEntity = userService.saveUser(userEntity);

        return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
    }

    @PutMapping("/users")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) {
        log.info("Updating user with id: " + user.getId() + " to: " + user);

        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity updatedUserEntity = userService.updateUser(userEntity);

        return new ResponseEntity<>(userMapper.mapTo(updatedUserEntity), HttpStatus.OK);
    }

//    @PatchMapping("/users/{id}")
//    @ResponseStatus(code = HttpStatus.OK)
//    public ResponseEntity<UserDto> updateUser(@PathVariable final Long id, @RequestBody UserDto user) {
//        log.info("Updating user with id: " + id + " to: " + user);
//
//        UserEntity userEntity = userMapper.mapFrom(user);
//        UserEntity updatedUserEntity = userService.updateUser(id, userEntity);
//
//    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final Long id) {
        log.info("Deleting user with id: " + id);

        userService.deleteUser(id);
    }
}
