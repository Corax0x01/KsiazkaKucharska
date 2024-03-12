package szymanski.jakub.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.domain.dto.UserDto;
import szymanski.jakub.backend.domain.entities.UserEntity;
import szymanski.jakub.backend.mappers.Mapper;
import szymanski.jakub.backend.services.UserService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    private final Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserDto> getUsers() {
        List<UserEntity> users = userService.findAll();
        return users.stream().map(userMapper::mapTo).toList();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable("id") Long id) {

        Optional<UserEntity> user = userService.find(id);
        return user.map(userEntity -> {
            UserDto userDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto user) {

        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity savedUserEntity = userService.save(userEntity);

        return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> fullUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto user) {

        if(!userService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.setId(id);
        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity updatedUserEntity = userService.save(userEntity);

        return new ResponseEntity<>(userMapper.mapTo(updatedUserEntity), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto user) {

        if(!userService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity updatedUser = userService.partialUpdate(id, userEntity);

        return new ResponseEntity<>(userMapper.mapTo(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
