package com.spring.vaidya.config;

import java.util.List;

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
 * Security configuration class for defining authentication and authorization rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthFilter authFilter;
	private final UserDetailsService userDetailsService;

	/**
	 * Constructor for SecurityConfig.
	 *
	 * @param authFilter          JWT authentication filter.
	 * @param userDetailsService  Service to load user-specific data.
	 */
	public SecurityConfig(JwtAuthFilter authFilter, UserDetailsService userDetailsService) {
		this.authFilter = authFilter;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Configures security settings including authentication, authorization, and session management.
	 *
	 * @param http The HttpSecurity object to configure security policies.
	 * @return The configured SecurityFilterChain.
	 * @throws Exception if an error occurs during configuration.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable()) // Disable CSRF protection (not needed for JWT-based authentication)
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
				.authorizeHttpRequests(authorize -> authorize
						// Publicly accessible endpoints
						.requestMatchers(
								"/user/new", "/doctor/confirm-account", "doctor/register",
								"/user/login", "/user/authenticate", "/user/welcome", "login/doctor"
						).permitAll()
						// Protected endpoints (require authentication)
						.requestMatchers(
								"/api/slots/{slotId}", "/api/slots/create", "/api/slots/{slotId}/availability",
								"doctor/all", "/api/slots/search?date=${selectedDate}&userId=${user.userId}"
						).authenticated()
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless sessions (no server-side session storage)
				.authenticationProvider(authenticationProvider()) // Set authentication provider
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before UsernamePasswordAuthenticationFilter
				.build();
	}

	/**
	 * Defines the authentication provider using DAO authentication with password encoding.
	 *
	 * @return The configured AuthenticationProvider.
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Defines the password encoder using BCrypt hashing algorithm.
	 *
	 * @return The password encoder bean.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures the authentication manager, integrating with the UserDetailsService and password encoder.
	 *
	 * @param http The HttpSecurity object for authentication setup.
	 * @return The configured AuthenticationManager.
	 * @throws Exception if an error occurs during configuration.
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
	 * Configures Cross-Origin Resource Sharing (CORS) settings.
	 *
	 * @return The configured CorsConfigurationSource.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.addAllowedOriginPattern("*"); // Allow all origins
	    configuration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
	    configuration.addAllowedHeader("*"); // Allow all headers
	    configuration.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration); // Apply this configuration to all endpoints
	    return source;
	}
}
