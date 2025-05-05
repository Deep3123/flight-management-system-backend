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

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.flight.management.filter.JwtFilter;
import com.flight.management.service.impl.CustomOAuth2UserService;
import com.flight.management.service.impl.CustomUserDetailsService;
import com.flight.management.service.impl.OAuth2AuthSuccessHandler;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//	@Autowired
//	private CustomUserDetailsService service;
//
//	@Autowired
//	private JwtFilter jwtFilter;
//
//	@Autowired
//	private CustomOAuth2UserService oAuth2UserService;
//
//	@Autowired
//	private OAuth2AuthSuccessHandler oAuth2AuthSuccessHandler;
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> {
//			var corsConfig = new org.springframework.web.cors.CorsConfiguration();
//			corsConfig.setAllowedOrigins(List.of("http://localhost:4200", "https://jetwayz.vercel.app", // Removed
//																										// trailing
//																										// slash
//					"http://jetwayz-deep3123s-projects.vercel.app" // Removed trailing slash
//			));
//			corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//			corsConfig.setAllowedHeaders(List.of("*"));
//			corsConfig.setExposedHeaders(List.of("X-Auth-Token")); // Expose session header
//			corsConfig.setAllowCredentials(true);
//			return corsConfig;
//		})).authorizeHttpRequests(auth -> auth
//				.requestMatchers("/user/register", "/user/login", "/user/forgot-password", "/user/reset-password/**",
//						"/captcha", "/oauth/complete-profile")
//				.permitAll()
//				.requestMatchers("/user/get-all-user-details", "/flight/add-flight-details",
//						"/flight/update-flight-details", "/flight/delete-flight-details/**",
//						"/flight/get-all-flights-details", "/flight/get-flights-details-by-flight-number/**",
//						"/contact/get-all-contact-us-details", "/contact/get-all-contact-us-details-by-name/**")
//				.hasAuthority("ADMIN").anyRequest().authenticated())
//				.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
//						.successHandler(oAuth2AuthSuccessHandler))
//				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); // Important change
//		return http.build();
//	}
//
//	@Bean
//	public CookieSerializer cookieSerializer() {
//		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//		serializer.setSameSite("None");
//		serializer.setUseSecureCookie(true);
//		serializer.setCookieName("JSESSIONID");
//		serializer.setCookiePath("/");
//		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // Allows subdomains
//		return serializer;
//	}
//
//	@Bean
//	public HttpSessionIdResolver httpSessionIdResolver() {
//		return HeaderHttpSessionIdResolver.xAuthToken();
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

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//	@Autowired
//	private CustomUserDetailsService service;
//
//	@Autowired
//	private JwtFilter jwtFilter;
//
//	@Autowired
//	private CustomOAuth2UserService oAuth2UserService;
//
//	@Autowired
//	private OAuth2AuthSuccessHandler oAuth2AuthSuccessHandler;
//
//	// Add authorization request repository for OAuth2
//	@Bean
//	public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
//		return new HttpCookieOAuth2AuthorizationRequestRepository();
//	}
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> {
//			var corsConfig = new org.springframework.web.cors.CorsConfiguration();
//			corsConfig.setAllowedOrigins(List.of("http://localhost:4200", "https://jetwayz.vercel.app",
//					"http://jetwayz-deep3123s-projects.vercel.app"));
//			corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//			corsConfig.setAllowedHeaders(List.of("*"));
//			corsConfig.setExposedHeaders(List.of("X-Auth-Token"));
//			corsConfig.setAllowCredentials(true);
//			return corsConfig;
//		})).authorizeHttpRequests(auth -> auth
//				.requestMatchers("/user/register", "/user/login", "/user/forgot-password", "/user/reset-password/**",
//						"/captcha", "/oauth/complete-profile", "/login", "/oauth2/authorization/**",
//						"/login/oauth2/code/**")
//				.permitAll()
//				.requestMatchers("/user/get-all-user-details", "/flight/add-flight-details",
//						"/flight/update-flight-details", "/flight/delete-flight-details/**",
//						"/flight/get-all-flights-details", "/flight/get-flights-details-by-flight-number/**",
//						"/contact/get-all-contact-us-details", "/contact/get-all-contact-us-details-by-name/**")
//				.hasAuthority("ADMIN").anyRequest().authenticated())
//				.oauth2Login(oauth2 -> oauth2
//						.authorizationEndpoint(authorization -> authorization
//								.authorizationRequestRepository(cookieAuthorizationRequestRepository()))
//						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
//						.successHandler(oAuth2AuthSuccessHandler))
//				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//				// Use STATELESS for JWT-based auth but still handle OAuth2 state
//				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//		return http.build();
//	}
//
//	@Bean
//	public CookieSerializer cookieSerializer() {
//		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//		serializer.setSameSite("None");
//		serializer.setUseSecureCookie(true);
//		serializer.setCookieName("JSESSIONID");
//		serializer.setCookiePath("/");
//		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
//		return serializer;
//	}
//
//	// Remove this for stateless JWT setup
//	// @Bean
//	// public HttpSessionIdResolver httpSessionIdResolver() {
//	// return HeaderHttpSessionIdResolver.xAuthToken();
//	// }
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private CustomUserDetailsService service;

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private CustomOAuth2UserService oAuth2UserService;

//	@Autowired
//	private CustomAuthorizationRequestResolver customAuthorizationRequestResolver;

	@Lazy
	@Autowired
	private OAuth2AuthSuccessHandler oAuth2AuthSuccessHandler;

	// Add authorization request repository for OAuth2
	@Bean
	public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	// Inject necessary services like ClientRegistrationRepository
//	@Lazy
//	@Autowired
//	private ClientRegistrationRepository clientRegistrationRepository;
//
//	@Bean
//	public ClientRegistrationRepository clientRegistrationRepository() {
//		return new InMemoryClientRegistrationRepository(clientRegistrations());
//	}
//
//	private ClientRegistration clientRegistrations() {
//		return ClientRegistration.withRegistrationId("google").clientId("YOUR_CLIENT_ID")
//				.clientSecret("YOUR_CLIENT_SECRET").scope("openid", "profile", "email")
//				.authorizationUri("https://accounts.google.com/o/oauth2/auth")
//				.tokenUri("https://oauth2.googleapis.com/token")
//				.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//				.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}").build();
//	}

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
//				.authorizeHttpRequests(auth -> auth
//						.requestMatchers("/user/register", "/user/login", "/user/forgot-password",
//								"/user/reset-password/**", "/captcha", "/oauth/complete-profile", "/login",
//								"/oauth2/authorization/**", "/login/oauth2/code/**")
//						.permitAll()
//						.requestMatchers("/user/get-all-user-details", "/flight/add-flight-details",
//								"/flight/update-flight-details", "/flight/delete-flight-details/**",
//								"/flight/get-all-flights-details", "/flight/get-flights-details-by-flight-number/**",
//								"/contact/get-all-contact-us-details", "/contact/get-all-contact-us-details-by-name/**")
//						.hasAuthority("ADMIN").anyRequest().authenticated())
//				.oauth2Login(oauth2 -> oauth2
//						.authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization")
//								.authorizationRequestRepository(cookieAuthorizationRequestRepository()))
//						.redirectionEndpoint(redirection -> redirection.baseUri("/login/oauth2/code/*"))
//						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
//						.successHandler(oAuth2AuthSuccessHandler))
//				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//		return http.build();
//	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/user/register", "/user/login", "/user/forgot-password",
								"/user/reset-password/**", "/captcha", "/oauth/complete-profile", "/login",
								"/oauth2/authorization/**", "/login/oauth2/code/**")
						.permitAll()
						.requestMatchers("/user/get-all-user-details", "/flight/add-flight-details",
								"/flight/update-flight-details", "/flight/delete-flight-details/**",
								"/flight/get-all-flights-details", "/flight/get-flights-details-by-flight-number/**",
								"/contact/get-all-contact-us-details", "/contact/get-all-contact-us-details-by-name/**")
						.hasAuthority("ADMIN").anyRequest().authenticated())
				.oauth2Login(oauth2 -> oauth2
						.authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization")
								.authorizationRequestResolver(customAuthorizationRequestResolver())
								.authorizationRequestRepository(cookieAuthorizationRequestRepository()))
						.redirectionEndpoint(redirection -> redirection.baseUri("/login/oauth2/code/*"))
						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
						.successHandler(oAuth2AuthSuccessHandler))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	public CustomAuthorizationRequestResolver customAuthorizationRequestResolver() {
		return new CustomAuthorizationRequestResolver(clientRegistrationRepository(), "/oauth2/authorization");
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		return new InMemoryClientRegistrationRepository(Arrays.asList(
				// Your client registrations - Google, etc.
				// You can move your client details to application properties and load from
				// there
				ClientRegistration.withRegistrationId("google")
						.clientId("${spring.security.oauth2.client.registration.google.client-id}")
						.clientSecret("${spring.security.oauth2.client.registration.google.client-secret}")
						.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
						.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
						.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}").scope("openid", "profile", "email")
						.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
						.tokenUri("https://www.googleapis.com/oauth2/v4/token")
						.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
						.userNameAttributeName(IdTokenClaimNames.SUB)
						.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs").clientName("Google").build()));
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://jetwayz.vercel.app",
				"http://jetwayz-deep3123s-projects.vercel.app"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("X-Auth-Token"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setSameSite("None");
		serializer.setUseSecureCookie(true);
		serializer.setCookieName("JSESSIONID");
		serializer.setCookiePath("/");
		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
		return serializer;
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