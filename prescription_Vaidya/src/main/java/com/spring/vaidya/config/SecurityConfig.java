package com.spring.vaidya.config;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.spring.vaidya.jwt.JwtAuthFilter;

/**
 * Security configuration class for handling authentication and authorization.
 * Uses JWT-based authentication with stateless sessions.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter authFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor-based dependency injection for JWT authentication filter and user details service.
     */
    public SecurityConfig(JwtAuthFilter authFilter, UserDetailsService userDetailsService) {
        this.authFilter = authFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures security settings, including authentication, authorization, and JWT filter.
     *
     * @param http The HTTP security object.
     * @return Configured SecurityFilterChain.
     * @throws Exception if an error occurs while building security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF (Cross-Site Request Forgery) as JWT is used
                .csrf(csrf -> csrf.disable())

                // Enable CORS (Cross-Origin Resource Sharing)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Define authorization rules for different endpoints
                .authorizeHttpRequests(authorize -> authorize
                        // Publicly accessible endpoints (no authentication required)
                        .requestMatchers("api/prescriptions/user/{userId}/date/{date}",
                                         "/user/new", "/doctor/confirm-account", 
                                         "doctor/register", "/user/login",
                                         "/doctor/email", "/user/authenticate", 
                                         "/user/welcome", "login/doctor")
                        .permitAll()

                        // Protected endpoints (authentication required)
                        .requestMatchers("/api/prescriptions/post", 
                                         "/api/prescriptions/{id}",
                                         "/doctor/all/f", "/user/protected")
                        .authenticated())

                // Configure session management to be stateless (since JWT is used)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Set authentication provider
                .authenticationProvider(authenticationProvider())

                // Add JWT authentication filter before the standard UsernamePasswordAuthenticationFilter
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    /**
     * Configures the authentication provider using DAO-based authentication.
     * Uses BCrypt password encoding for hashing passwords.
     *
     * @return Configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Defines the password encoder used for encoding and verifying user passwords.
     *
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures and provides an AuthenticationManager bean for managing authentication.
     *
     * @param http The HTTP security object.
     * @return Configured AuthenticationManager.
     * @throws Exception if an error occurs while building authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
    
    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings to allow external frontend applications to communicate with the backend.
     *
     * @return Configured CorsConfigurationSource.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Allow requests from any origin
        configuration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials such as cookies and authorization headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS settings to all endpoints
        return source;
    }
}
