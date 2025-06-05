package se.sti.fredrik.secureapp.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code OpenApiConfig} sets up Swagger security configuration for the application
 * <p>
 * It defines a security scheme for JWT bearer token authentication, enabling secured endpoints
 * to be tested directly from Swagger
 */
@Configuration
public class OpenApiConfig {


    /**
     * Configures the OpenAPI with JWT bearer token authentication
     * @return configured {@link OpenAPI} (Swagger) instance with security implements
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info().title("SecureApp API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
