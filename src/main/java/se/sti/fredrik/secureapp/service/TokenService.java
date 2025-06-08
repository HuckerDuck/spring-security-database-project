package se.sti.fredrik.secureapp.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import se.sti.fredrik.secureapp.Config.SecurityConfig;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

/**
 * Service class responsible for generating JWT tokens for authenticating users
 * <p>
 * Uses the configured {@link JwtEncoder} to create a signed JWT
 * that includes user details and granted authorities
 * </p>
 */
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    /**
     * Initializes the TokenService with a {@link JwtEncoder}
     * @param jwtEncoder the encoder used to generate JWT tokens
     */
    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a token for the provided {@link Authentication} object.
     * The token includes issuer, issued time, expiration time and a scope with user authorities
     * @param authentication the authenticated user's details
     * @return a signed JWT token as a String
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.
                builder().
                issuer("self").
                issuedAt(now).
                subject(authentication.getName()).
                expiresAt(now.plus(1, ChronoUnit.HOURS))
                .claim("scope", scope).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
