package szymanski.jakub.backend.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.common.exceptionhandler.ExceptionResponse;
import szymanski.jakub.backend.role.exceptions.RoleNotFoundException;
import szymanski.jakub.backend.role.repositories.RoleRepository;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.services.UserService;

import java.util.List;

@Tag(name = "Users")
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    /**
     * Fetches all users.
     *
     * @return  {@link ResponseEntity} containing list of all users
     */
    @Operation(summary = "Fetches all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users fetched",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
            )),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {

        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Fetches single user with given ID.
     *
     * @param   id  ID of the user
     * @return      {@link ResponseEntity} containing {@link UserDto} object with given ID
     */
    @Operation(summary = "Fetches single user with given ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the user",
            content = { @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class)
            )}),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @Parameter(description = "ID of the user to be fetched")
            @PathVariable("id")
            Long id) {

        UserDto user = userService.find(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Creates a user.
     *
     * @param   user    {@link UserDto} object that contains data for creating user
     * @return          {@link ResponseEntity} containing ID of created user
     */
    @Operation(summary = "Creates a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created. Responds with user ID",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "USER role not found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Long> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Object that contains data for creating user",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = """
                        {
                            "username": "New user",
                            "password": "P@ssw0rd",
                            "email": "test@email.com"
                        }
                        """)))
            @RequestBody UserDto user) {

        Long savedUserId = userService.save(
                user,
                false,
                false,
                List.of(roleRepository.findByName("USER").orElseThrow(
                        () -> new RoleNotFoundException("Role USER not found")
                )));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserId);
    }

    /**
     * Updates user with given ID.
     *
     * @param   id      ID of the user to update
     * @param   user    {@link UserDto} object that contains data for updating user
     * @return          {@link ResponseEntity} containing ID of updated user
     */
    @Operation(summary = "Updates user with given ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated. Responds with user ID",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateUser(
            @Parameter(description = "ID of the user to update")
            @PathVariable("id")
            Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Object that contains data for updating user",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = """
                        {
                            "password": "NewP@ssw0rd",
                        }
                        """)
                )
            )
            @RequestBody UserDto user) {

        Long updatedUserId = userService.partialUpdate(id, user);
        return ResponseEntity.ok(updatedUserId);
    }

    /**
     * Deletes user with given ID.
     *
     * @param   id  ID of the user to delete
     */
    @Operation(summary = "Deletes user with given ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(
            @Parameter(description = "ID of the user to delete")
            @PathVariable("id") Long id) {
        userService.delete(id);
    }
}
