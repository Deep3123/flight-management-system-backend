//package com.flight.management.configuration;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.flight.management.filter.JwtFilter;
//import com.flight.management.service.impl.CustomUserDetailsService;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	@Autowired
//	private CustomUserDetailsService service;
//
//	@Autowired
//	private JwtFilter jwtFilter;
//
////	@Autowired
////	private WebConfig webConfig;
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////		System.err.println("Control is in security chain filter.");
//
//		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> {
//			var corsConfig = new org.springframework.web.cors.CorsConfiguration();
//			corsConfig.setAllowedOrigins(List.of("http://localhost:4200", "https://jetwayz.vercel.app/",
//					"http://jetwayz-deep3123s-projects.vercel.app/")); 
//			corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//			corsConfig.setAllowedHeaders(List.of("*"));
//			corsConfig.setAllowCredentials(true);
//			return corsConfig;
//		})).authorizeHttpRequests(auth -> auth
//				.requestMatchers("/user/register", "/user/login", "/user/forgot-password", "/user/reset-password/**",
//						"/captcha")
//				.permitAll()
//				.requestMatchers("/user/get-all-user-details", "/flight/add-flight-details",
//						"/flight/update-flight-details", "/flight/delete-flight-details/**",
//						"/flight/get-all-flights-details", "/flight/get-flights-details-by-flight-number/**",
//						"/contact/get-all-contact-us-details", "/contact/get-all-contact-us-details-by-name/**")
//				.hasAuthority("ADMIN").anyRequest().authenticated())
////				.formLogin(Customizer.withDefaults())
////				.httpBasic(Customizer.withDefaults())
//				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
////				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
//
//		return http.build();
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//		auth.setUserDetailsService(service);
//		auth.setPasswordEncoder(passwordEncoder());
//		return auth;
//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
//		return auth.getAuthenticationManager();
//	}
//}

package com.flight.management.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import com.flight.management.filter.JwtFilter;
import com.flight.management.service.impl.CustomOAuth2UserService;
import com.flight.management.service.impl.CustomUserDetailsService;
import com.flight.management.service.impl.OAuth2AuthSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private CustomUserDetailsService service;

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private CustomOAuth2UserService oAuth2UserService;

	@Autowired
	private OAuth2AuthSuccessHandler oAuth2AuthSuccessHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> {
			var corsConfig = new org.springframework.web.cors.CorsConfiguration();
			corsConfig.setAllowedOrigins(List.of("http://localhost:4200", "https://jetwayz.vercel.app", // Removed
																										// trailing
																										// slash
					"http://jetwayz-deep3123s-projects.vercel.app" // Removed trailing slash
			));
			corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			corsConfig.setAllowedHeaders(List.of("*"));
			corsConfig.setExposedHeaders(List.of("X-Auth-Token")); // Expose session header
			corsConfig.setAllowCredentials(true);
			return corsConfig;
		})).authorizeHttpRequests(auth -> auth
				.requestMatchers("/user/register", "/user/login", "/user/forgot-password", "/user/reset-password/**",
						"/captcha")
				.permitAll()
				.requestMatchers("/user/get-all-user-details", "/flight/add-flight-details",
						"/flight/update-flight-details", "/flight/delete-flight-details/**",
						"/flight/get-all-flights-details", "/flight/get-flights-details-by-flight-number/**",
						"/contact/get-all-contact-us-details", "/contact/get-all-contact-us-details-by-name/**")
				.hasAuthority("ADMIN").anyRequest().authenticated())
				.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
						.successHandler(oAuth2AuthSuccessHandler))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); // Important change
		return http.build();
	}

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setSameSite("None");
		serializer.setUseSecureCookie(true);
		serializer.setCookieName("JSESSIONID");
		serializer.setCookiePath("/");
		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // Allows subdomains
		return serializer;
	}

	@Bean
	public HttpSessionIdResolver httpSessionIdResolver() {
		return HeaderHttpSessionIdResolver.xAuthToken();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(service);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();
	}
}