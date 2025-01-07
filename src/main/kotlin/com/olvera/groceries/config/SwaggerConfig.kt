package com.olvera.groceries.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class SwaggerConfig {

    @Bean
    fun springOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Grocery Shopping")
                    .description("This API exposes RESTful authentication and authorization via JWT.")
                    .version("1.0.0")
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://github.com/{your-user}/{your-repo}/blob/{your-branch}/LICENSE.txt")
                    )
            )
            .addServersItem(
                Server()
                    .url("http://localhost:9000")
                    .description("DEVELOPMENT")
            )
            .addServersItem(
                Server()
                    .url("https://xyz.com")
                    .description("PRODUCTION")
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "BearerAuth",
                        SecurityScheme()
                            .name("BearerAuth")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
            .addSecurityItem(
                SecurityRequirement()
                    .addList("BearerAuth")
            )
    }
}