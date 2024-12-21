package com.ms.service_app.config;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ms.service_app.filter.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ms.service_app.jwt.CustomJwtAuthenticationConverter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {

    private final OAuth2ResourceServerProperties properties;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public SecurityConfig(OAuth2ResourceServerProperties properties,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.properties = properties;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    BearerTokenResolver bearerTokenResolver() {
        // look in both app.at cookie and Authorization header
        BearerTokenResolver bearerTokenResolver = new BearerTokenResolver () {
            public String resolve(HttpServletRequest request) {
               Cookie[] cookies = request.getCookies();

               if (cookies != null) {
                   Optional<Cookie> cookie = Arrays.stream(cookies)
                       .filter(name -> name.getName().equals("app.at"))
                       .findFirst();
                   if (cookie.isPresent()) {
                       return cookie.get().getValue();
                   }
               }

               // handles authorization header
               DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
               return defaultBearerTokenResolver.resolve(request);
            }
        };
        return bearerTokenResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withIssuerLocation(properties.getJwt().getIssuerUri()).build();
//    }
}
