package szymanski.jakub.backend.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtFilter filter;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesRequestWithValidBearerToken() throws Exception {
        MockHttpServletRequest request = request("/recipes");
        request.addHeader("Authorization", "Bearer token-value");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        UserDetails user = User.withUsername("chef").password("hash").authorities("USER").build();
        when(jwtService.extractUsername("token-value")).thenReturn("chef");
        when(userDetailsService.loadUserByUsername("chef")).thenReturn(user);
        when(jwtService.isTokenValid("token-value", user)).thenReturn(true);

        filter.doFilter(request, response, chain);

        assertSame(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doesNotLoadUserWithoutBearerTokenOrOnAuthenticationRoutes() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        MockHttpServletRequest noHeaderRequest = request("/recipes");
        MockHttpServletRequest authRequest = request("/auth/login");
        authRequest.addHeader("Authorization", "Bearer token-value");

        filter.doFilter(noHeaderRequest, response, chain);
        filter.doFilter(authRequest, response, chain);

        verifyNoInteractions(jwtService, userDetailsService);
        verify(chain).doFilter(noHeaderRequest, response);
        verify(chain).doFilter(authRequest, response);
    }

    @Test
    void continuesWithoutAuthenticationWhenTokenIsInvalid() throws Exception {
        MockHttpServletRequest request = request("/recipes");
        request.addHeader("Authorization", "Bearer token-value");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        UserDetails user = User.withUsername("chef").password("hash").authorities("USER").build();
        when(jwtService.extractUsername("token-value")).thenReturn("chef");
        when(userDetailsService.loadUserByUsername("chef")).thenReturn(user);
        when(jwtService.isTokenValid("token-value", user)).thenReturn(false);

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    private MockHttpServletRequest request(String servletPath) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath(servletPath);
        return request;
    }
}
