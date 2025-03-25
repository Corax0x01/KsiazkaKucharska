package szymanski.jakub.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//    TODO: add functionality that will check if token already exists, if yes send it to user

/**
 * Service responsible for handling JSON Web Token
 */
@Service
public class JwtService {

    /**
     * Token expiration time in milliseconds.
     */
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Generates JSON Web Token.
     *
     * @param   userDetails details about the user for whom the token is to be generated
     * @return              generated JWT
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates JSON Web Token.
     *
     * @param   claims          information about JWT subject
     * @param   userDetails     details about the user for whom the token is to be generated
     * @return                  generated JWT
     */
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration);
    }

    /**
     * Builds JSON Web Token containing given data.
     *
     * @param claims            information about JWT subject
     * @param userDetails       details about the user for whom the token is to be generated
     * @param jwtExpiration     time in millis after which token will expire
     * @return                  built JWT
     */
    public String buildToken(Map<String, Object> claims, UserDetails userDetails, long jwtExpiration) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Verifies if JSON Web Token is valid.
     *
     * @param   token           JWT to verify
     * @param   userDetails     details about the user
     * @return                  <code>true</code> if token is valid, <code>false</code> if not
     */
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Verifies if JSON Web Token has expired.
     *
     * @param   token   JWT to be verified
     * @return          <code>true</code> if token is expired <code>false</code> if not
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts username from JSON Web Token.
     *
     * @param   token   JWT to extract username from
     * @return          username of user associated with this JWT
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts specified claim from JSON Web Token.
     *
     * @param   token           JWT
     * @param   claimResolver   method for extracting specific claim from all claims
     * @return                  claim extracted from given JWT
     * @param   <T>             type of extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Extracts all claims from JSON Web Token.
     *
     * @param   token   JWT token
     * @return          claims extracted from given token
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
