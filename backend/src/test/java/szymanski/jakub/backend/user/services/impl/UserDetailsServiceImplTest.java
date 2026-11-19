package szymanski.jakub.backend.user.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;
import szymanski.jakub.backend.user.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void returnsUserDetailsForKnownUsername() {
        UserEntity user = UserEntity.builder().username("cook").password("hash").build();
        when(userRepository.findByUsername("cook")).thenReturn(Optional.of(user));

        assertSame(user, userDetailsService.loadUserByUsername("cook"));
    }

    @Test
    void throwsForUnknownUsername() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing"));
    }
}
