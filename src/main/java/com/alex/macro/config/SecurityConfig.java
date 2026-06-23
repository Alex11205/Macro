package com.alex.macro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

//                        .requestMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE,"/api/users/{id}").hasRole("ADMIN")
//                        .requestMatchers("/api/foods/{foodName}").hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.PUT,"/api/users").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET,"/api/users/profile", "/api/foods/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.POST,"/api/foods").hasRole("USER")
//                        .requestMatchers("/api/favorites/favorites/{foodId}", "favoriteList").hasRole("USER")

                                .requestMatchers("/**").permitAll()
                                .anyRequest().permitAll()
//                        .requestMatchers("/api/users/login", "/api/users/register").permitAll()
//                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());
//                .formLogin(form -> form
//                        .loginPage("/Signin")
//                        .loginProcessingUrl("/api/users/login")
//                        .defaultSuccessUrl("/profile", true)
//                        .permitAll()
//                )
//                .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Cache-Control"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(12);
    }
}
