package com.olvera.groceries.config

import com.olvera.groceries.web.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtFilter: JwtAuthenticationFilter,
    private val authProvider: AuthenticationProvider
) {

    @Bean
    fun filterRequests(https: HttpSecurity): SecurityFilterChain {
        return https
            .csrf { it.disable() }
            .cors { it.configurationSource(setupCorsConfigurationSource()) }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "api/v1/auth/**",
                    "api/swagger-ui/**",
                    "api/v3/api-docs",
                    "api/swagger-ui.html",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "api/v3/api-docs/swagger-config",
                    "api/open-api.yml"
                ).permitAll().anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun setupCorsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }

        return source
    }

}