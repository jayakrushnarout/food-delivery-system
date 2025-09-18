package com.springcloud.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity

public class SecurityConfig {

    @Value("${spring.app.jwtSecret}")
    private String secret; // Base64 encoded secret key

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(auth -> auth
                        .pathMatchers("/auth/**").permitAll()  // login/signup open
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/orders/**").hasAnyRole("CUSTOMER", "ADMIN") // Example: restrict orders to USER and ADMIN roles
                        .pathMatchers("/payments/**").hasRole("ADMIN")
                        .pathMatchers("/restaurants/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .pathMatchers("/dlq/**").hasRole("ADMIN")

                        .anyExchange().authenticated() // Changed from permitAll() to authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(reactiveJwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);

        SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        var converter = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaimAsStringList("role");
            if (authorities == null) {
                authorities = List.of();
            }

            return authorities.stream()
                    .map(authority -> {
                        // Ensure authorities have ROLE_ prefix for Spring Security role checking
                        if (!authority.startsWith("ROLE_")) {
                            return new SimpleGrantedAuthority("ROLE_" + authority);
                        }
                        return new SimpleGrantedAuthority(authority);
                    })
                    .collect(Collectors.toList());
        });

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}