package com.example.vaidya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.vaidya.jwt.JwtAuthFilter;

/**
 * 📌 Security Configuration Class for Spring Security Setup.
 * Implements JWT-based authentication and role-based access control.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter authFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor Injection for dependencies.
     * 
     * @param authFilter        The JWT authentication filter.
     * @param userDetailsService Service to fetch user details.
     */
    public SecurityConfig(JwtAuthFilter authFilter, UserDetailsService userDetailsService) {
        this.authFilter = authFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 🔒 Configures security rules for HTTP requests.
     * - Disables CSRF (since we're using JWT).
     * - Defines public and protected endpoints.
     * - Implements stateless session management.
     * - Adds JWT authentication filter.
     *
     * @param http The HttpSecurity object.
     * @return Configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // 🔴 Disabling CSRF since JWT is used
                .authorizeHttpRequests(authorize -> authorize
                        // ✅ Public Endpoints (No Authentication Required)
                        .requestMatchers("/user/new", "/doctor/confirm-account", "doctor/register",
                                         "/user/login", "/doctor/email", "/user/authenticate",
                                         "/user/welcome", "login/doctor","/ums/**").permitAll()

                        // 🔒 Protected Endpoints (Authentication Required)
                        .requestMatchers("/users/filter/**", "/doctor/all/f",
                                         "/user/protected", "/users","/users/sorted/**","users").authenticated())

                // 🛡️ Enable Stateless Session (since JWT is used)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 🔄 Set Authentication Provider
                .authenticationProvider(authenticationProvider())

                // 🛠️ Add JWT Authentication Filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    /**
     * 🔑 Configures Authentication Provider using DAO Authentication.
     * Uses BCrypt for password encoding and UserDetailsService for user retrieval.
     *
     * @return Configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Fetches user details
        authProvider.setPasswordEncoder(passwordEncoder()); // Encrypts passwords
        return authProvider;
    }

    /**
     * 🔐 Password Encoder using BCrypt.
     * - Ensures secure password storage and verification.
     * 
     * @return PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 🔄 Authentication Manager Bean.
     * - Manages authentication process using UserDetailsService and password encoder.
     * 
     * @param http The HttpSecurity object.
     * @return Configured AuthenticationManager.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService) // Uses custom UserDetailsService
                .passwordEncoder(passwordEncoder()) // Uses BCrypt for password hashing
                .and()
                .build();
    }
}
