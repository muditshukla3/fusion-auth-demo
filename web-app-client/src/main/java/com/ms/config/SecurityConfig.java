package com.ms.config;

import com.ms.controller.CustomLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository repo) throws Exception {

        http
                .authorizeRequests(a -> a
                        .antMatchers("/", "/images/**", "/css/**","/initiate-logout")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                        .oauth2Login(Customizer.withDefaults());
        http.logout(logout -> logout
                .logoutUrl("/initiate-logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new CustomLogoutHandler();
    }

}
