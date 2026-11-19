package szymanski.jakub.backend.user.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.role.exceptions.RoleNotFoundException;
import szymanski.jakub.backend.role.repositories.RoleRepository;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.services.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private RoleRepository roleRepository;
    private UserController controller;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        roleRepository = mock(RoleRepository.class);
        controller = new UserController(userService, roleRepository);
    }

    @Test
    void getsAllUsersAndUserById() {
        List<UserDto> users = List.of(UserDto.builder().id(1L).username("chef").build());
        when(userService.findAll()).thenReturn(users);
        when(userService.find(1L)).thenReturn(users.getFirst());

        assertEquals(ResponseEntity.ok(users), controller.getUsers());
        assertEquals(ResponseEntity.ok(users.getFirst()), controller.getUser(1L));
    }

    @Test
    void createsUserWithDefaultAccountStateAndUserRole() {
        UserDto user = UserDto.builder().username("chef").build();
        RoleEntity role = RoleEntity.builder().name("USER").build();
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userService.save(user, false, false, List.of(role))).thenReturn(17L);

        ResponseEntity<Long> response = controller.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(17L, response.getBody());
        verify(userService).save(user, false, false, List.of(role));
    }

    @Test
    void propagatesMissingUserRoleAndUpdatesUser() {
        UserDto patch = UserDto.builder().email("new@example.com").build();
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());
        when(userService.partialUpdate(4L, patch)).thenReturn(4L);

        assertThrows(RoleNotFoundException.class, () -> controller.createUser(patch));
        assertEquals(ResponseEntity.ok(4L), controller.partialUpdateUser(4L, patch));
    }

    @Test
    void deletesUser() {
        controller.deleteUser(8L);

        verify(userService).delete(8L);
    }
}
