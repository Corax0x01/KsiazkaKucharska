package szymanski.jakub.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 60_000L);
        ReflectionTestUtils.setField(jwtService, "secretKey",
                Base64.getEncoder().encodeToString("0123456789abcdef0123456789abcdef".getBytes()));
        user = User.withUsername("chef").password("hash").authorities("USER", "ADMIN").build();
    }

    @Test
    void generatedTokenContainsSubjectClaimsAndAuthorities() {
        String token = jwtService.generateToken(Map.of("scope", "recipes"), user);
        Claims claims = jwtService.extractAllClaims(token);

        assertEquals("chef", jwtService.extractUsername(token));
        assertEquals("recipes", claims.get("scope"));
        List<?> authorities = claims.get("authorities", List.class);
        assertEquals(Set.of("USER", "ADMIN"), Set.copyOf(authorities));
        assertTrue(jwtService.isTokenValid(token, user));
        assertFalse(jwtService.isTokenValid(token,
                User.withUsername("another-chef").password("hash").authorities("USER").build()));
    }

    @Test
    void rejectsExpiredTokens() {
        String token = jwtService.buildToken(Map.of(), user, -1L);

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, user));
    }
}
