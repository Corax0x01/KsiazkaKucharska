package szymanski.jakub.backend.user.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;
import szymanski.jakub.backend.user.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllMapsEveryUser() {
        UserEntity first = UserEntity.builder().id(1L).username("first").build();
        UserEntity second = UserEntity.builder().id(2L).username("second").build();
        when(userRepository.findAll()).thenReturn(List.of(first, second));
        when(userMapper.mapTo(first)).thenReturn(UserDto.builder().id(1L).username("first").build());
        when(userMapper.mapTo(second)).thenReturn(UserDto.builder().id(2L).username("second").build());

        assertEquals(List.of(
                UserDto.builder().id(1L).username("first").build(),
                UserDto.builder().id(2L).username("second").build()
        ), userService.findAll());
    }

    @Test
    void findByIdAndUsernameMapFoundUsersAndThrowWhenAbsent() {
        UserEntity entity = UserEntity.builder().id(7L).username("cook").build();
        UserDto dto = UserDto.builder().id(7L).username("cook").build();
        when(userRepository.findById(7L)).thenReturn(Optional.of(entity));
        when(userRepository.findByUsername("cook")).thenReturn(Optional.of(entity));
        when(userMapper.mapTo(entity)).thenReturn(dto);

        assertSame(dto, userService.find(7L));
        assertSame(dto, userService.find("cook"));
        assertThrows(UserNotFoundException.class, () -> userService.find(99L));
        assertThrows(UserNotFoundException.class, () -> userService.find("missing"));
    }

    @Test
    void saveMapsEncodesPasswordAndSetsAccountAndRoles() {
        UserDto dto = UserDto.builder().username("cook").password("plain").build();
        RoleEntity role = RoleEntity.builder().name("USER").build();
        UserEntity mapped = UserEntity.builder().username("cook").password("plain").build();
        UserEntity saved = UserEntity.builder().id(12L).build();
        when(userMapper.mapFrom(dto)).thenReturn(mapped);
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepository.save(mapped)).thenReturn(saved);

        assertEquals(12L, userService.save(dto, true, false, List.of(role)));

        assertEquals("encoded", mapped.getPassword());
        assertTrue(mapped.getEnabled());
        assertFalse(mapped.getLocked());
        assertEquals(List.of(role), mapped.getRoles());
        verify(userRepository).save(mapped);
    }

    @Test
    void partialUpdateChangesOnlyProvidedFields() {
        UserEntity existing = UserEntity.builder()
                .id(4L).username("old").password("secret").email("old@example.com").build();
        UserDto patch = UserDto.builder().email("new@example.com").build();
        UserEntity mappedPatch = UserEntity.builder().email("new@example.com").build();
        when(userMapper.mapFrom(patch)).thenReturn(mappedPatch);
        when(userRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        assertEquals(4L, userService.partialUpdate(4L, patch));

        assertEquals(4L, patch.getId());
        assertEquals("old", existing.getUsername());
        assertEquals("secret", existing.getPassword());
        assertEquals("new@example.com", existing.getEmail());
        verify(userRepository).save(existing);
    }

    @Test
    void partialUpdateAndDeleteThrowWhenUserDoesNotExist() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());
        when(userRepository.existsById(5L)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> userService.partialUpdate(5L, UserDto.builder().username("new").build()));
        assertThrows(UserNotFoundException.class, () -> userService.delete(5L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteByDtoChecksExistenceAndDeletesMappedEntity() {
        UserDto dto = UserDto.builder().id(8L).build();
        UserEntity entity = UserEntity.builder().id(8L).build();
        when(userMapper.mapFrom(dto)).thenReturn(entity);
        when(userRepository.existsById(8L)).thenReturn(true);

        userService.delete(dto);

        verify(userRepository).delete(entity);
    }

    @Test
    void findsUsersByRolesAndChecksExistence() {
        RoleEntity role = RoleEntity.builder().name("USER").build();
        UserEntity user = UserEntity.builder().id(5L).build();
        UserDto dto = UserDto.builder().id(5L).build();
        when(userRepository.findByRoles(List.of(role))).thenReturn(List.of(user));
        when(userMapper.mapTo(user)).thenReturn(dto);
        when(userRepository.existsById(5L)).thenReturn(true);

        assertEquals(List.of(dto), userService.findByRoles(List.of(role)));
        assertTrue(userService.exists(5L));
        assertTrue(userService.exists(user));
    }

    @Test
    void deletesExistingUserByIdAndRejectsMissingDto() {
        UserDto dto = UserDto.builder().id(10L).build();
        UserEntity entity = UserEntity.builder().id(10L).build();
        when(userRepository.existsById(10L)).thenReturn(true, false);
        when(userMapper.mapFrom(dto)).thenReturn(entity);

        userService.delete(10L);

        verify(userRepository).deleteById(10L);
        assertThrows(UserNotFoundException.class, () -> userService.delete(dto));
        verify(userRepository, never()).delete(entity);
    }
}
