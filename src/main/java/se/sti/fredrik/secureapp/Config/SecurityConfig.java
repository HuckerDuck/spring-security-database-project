package se.sti.fredrik.secureapp.Config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.security.KeyPairGenerator;
import java.security.KeyPair;

import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import se.sti.fredrik.secureapp.Service.MyUserDetailsService;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.UUID;

/**
 * {@code SecurityConfig} is the central security configuration for the application.
 *
 * <p>This configuration sets up:</p>
 * <ul>
 *   <li>JSON Web Token authentication with RSA key pair (public/private)</li>
 *   <li>Custom user authentication with {@link MyUserDetailsService}</li>
 *   <li>Authorization using scopes via JWT</li>
 *   <li>Role-based access to endpoints</li>
 *   <li>Stateless session management</li>
 * </ul>
 *
 * <p>Endpoints under <code>/admin/**</code> require ADMIN role,
 * <code>/user/**</code> require USER or ADMIN. Swagger endpoints are publicly accessible for testing purposes.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Generates an RSA key pair using for verifying JWT Tokens
     * @return the generated RSA key pair
     */
    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    /**
     * Creates a JWK source from the generated RSA key pair
     * @param keyPair the RSA key pair
     * @return a JWK source based on the provided key pair
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    /**
     * Configures the JWT encoder using the JWK source
     * @param jwkSource the source of JWK keys
     * @return a NimbusJwtEncoder instance
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Configures a JWT decoder using the RSA public key
     * @param keyPair the RSA key pair
     * @return a configured NimbusJwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
    }

    /**
     * Instantiate a password encoder
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication manager with custom user details service and password encoder
     * @param userDetailsService the user details service
     * @param passwordEncoder the password encoder
     * @return a configured AuthenticationManager instance
     */
    @Bean
    public AuthenticationManager authenticationManager(
            MyUserDetailsService userDetailsService, //Kolla med håkan
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    /**
     * Configures how scopes are converted to authorities for roll-based access
     * @return a JwtAuthenticationManager
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }


    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: Please provide a valid token.\"}");
        };
    }

    /**
     * Configures HTTP security for the application.
     * <p>Includes:</p>
     * <ul>
     *     <li>Disabling CSRF</li>
     *     <li>Stateless session policy</li>
     *     <li>Endpoint role-based access</li>
     *     <li>Enabling JWT using resource server</li>
     * </ul>
     *
     * @param http the HttpSecurity builder
     * @return a configured SecurityFilterChain
     * @throws Exception if configuration error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(RoutePaths.ADMIN_BASE + "/**").hasRole("ADMIN")
                .requestMatchers(RoutePaths.USER_BASE).hasAnyRole("USER", "ADMIN")
                .requestMatchers(
                        "/userController/register",
                        "/admin/user/register",
                        RoutePaths.AUTH_BASE + "/request-token",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml"
                ).permitAll().anyRequest().authenticated());

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }
}
