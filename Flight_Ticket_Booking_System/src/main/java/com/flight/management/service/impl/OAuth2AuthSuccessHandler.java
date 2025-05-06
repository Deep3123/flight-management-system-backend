package com.flight.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.flight.management.configuration.HttpCookieOAuth2AuthorizationRequestRepository;
import com.flight.management.domain.UserEntity;
import com.flight.management.repo.UserRepo;
import com.flight.management.service.UserService;
import com.flight.management.util.CookieUtils;
import com.flight.management.util.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

//@Component
//public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//	@Autowired
//	private JwtService jwtService;
//
//	private final String frontendBaseUrl = "https://localhost:4200";
//
////	private final String frontendBaseUrl = "https://jetwayz.vercel.app"; // Change this to your frontend URL
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//
//		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//		OAuth2User oauth2User = oauthToken.getPrincipal();
//		Map<String, Object> attributes = oauth2User.getAttributes();
//
//		// Get email as username - will serve as the principal identifier
//		String email = (String) attributes.get("email");
//
//		// Check if user needs to complete their profile
//		boolean requiresProfileCompletion = attributes.containsKey("requiresProfileCompletion")
//				? (boolean) attributes.get("requiresProfileCompletion")
//				: false;
//
//		// Generate JWT token
//		String token = jwtService.generateToken(email);
//
//		// Redirect to appropriate page
//		String redirectUrl;
//		if (requiresProfileCompletion) {
//			// Redirect to profile completion form
//			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/complete-profile")
//					.queryParam("token", token).queryParam("email", email).queryParam("name", attributes.get("name"))
//					.build().toUriString();
//		} else {
//			// Redirect to home page with token
//			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/oauth-callback")
//					.queryParam("token", token).build().toUriString();
//		}
//
//		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//	}
//}

//	@Value("${app.frontend.url.local}")
//	private String localFrontendUrl;
//
//	@Value("${app.frontend.url.prod}")
//	private String prodFrontendUrl;
//
//	private String getFrontendBaseUrl(HttpServletRequest request) {
//		String origin = request.getHeader("Origin");
//		if (origin != null && origin.contains("localhost")) {
//			return localFrontendUrl;
//		}
//		return prodFrontendUrl;
//	}
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//
//		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//		OAuth2User oauth2User = oauthToken.getPrincipal();
//		Map<String, Object> attributes = oauth2User.getAttributes();
//
//		String email = (String) attributes.get("email");
//		boolean requiresProfileCompletion = attributes.containsKey("requiresProfileCompletion")
//				? (boolean) attributes.get("requiresProfileCompletion")
//				: false;
//
//		String token = jwtService.generateToken(email);
//
//		// Use the appropriate frontend URL
//		String frontendBaseUrl = getFrontendBaseUrl(request);
//
//		String redirectUrl;
//		if (requiresProfileCompletion) {
//			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/complete-profile")
//					.queryParam("token", token).queryParam("email", email).queryParam("name", attributes.get("name"))
//					.build().toUriString();
//		} else {
//			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/oauth-callback")
//					.queryParam("token", token).build().toUriString();
//		}
//
//		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//	}
//}

//@Component

//public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//	@Autowired
//	private JwtService jwtService;
//
//	@Value("${app.frontend.url.local}")
//	private String localFrontendUrl;
//
//	@Value("${app.frontend.url.prod}")
//	private String prodFrontendUrl;
//
//	// Consider adding a more robust environment detection method
//	private String getFrontendBaseUrl(HttpServletRequest request) {
//		String origin = request.getHeader("Origin");
//		String referer = request.getHeader("Referer");
//
//		// Check both Origin and Referer headers for better reliability
//		if ((origin != null && origin.contains("localhost")) || (referer != null && referer.contains("localhost"))) {
//			System.err.println("origin: " + origin);
//			System.err.println("referer: " + referer);
//			return localFrontendUrl;
//		}
//		return prodFrontendUrl;
//	}
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		if (!(authentication instanceof OAuth2AuthenticationToken)) {
//			super.onAuthenticationSuccess(request, response, authentication);
//			return;
//		}
//
//		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//		OAuth2User oauth2User = oauthToken.getPrincipal();
//		Map<String, Object> attributes = oauth2User.getAttributes();
//
//		// Safely extract attributes with null checks
//		String email = getAttributeAsString(attributes, "email");
//		String name = getAttributeAsString(attributes, "name");
//		boolean requiresProfileCompletion = getAttributeAsBoolean(attributes, "requiresProfileCompletion", false);
//
//		if (email == null || email.isEmpty()) {
//			// Handle missing email case
//			getRedirectStrategy().sendRedirect(request, response,
//					getFrontendBaseUrl(request) + "/oauth-error?reason=missing-email");
//			return;
//		}
//
//		String token = jwtService.generateToken(email);
//		String frontendBaseUrl = getFrontendBaseUrl(request);
//		String redirectUrl;
//
//		if (requiresProfileCompletion) {
//			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/complete-profile")
//					.queryParam("token", token).queryParam("email", email).queryParam("name", name).build()
//					.toUriString();
//		} else {
//			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/oauth-callback")
//					.queryParam("token", token).build().toUriString();
//		}
//
//		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//	}
//
//	// Helper methods for safer attribute access
//	private String getAttributeAsString(Map<String, Object> attributes, String key) {
//		Object value = attributes.get(key);
//		return value instanceof String ? (String) value : null;
//	}
//
//	private boolean getAttributeAsBoolean(Map<String, Object> attributes, String key, boolean defaultValue) {
//		Object value = attributes.get(key);
//		return value instanceof Boolean ? (Boolean) value : defaultValue;
//	}
//}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

//@Component
//public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//	@Autowired
//	private JwtService jwtService;
//
//	@Autowired
//	private HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;
//
//	@Value("${app.frontend.url.local}")
//	private String localFrontendUrl;
//
//	@Value("${app.frontend.url.prod}")
//	private String prodFrontendUrl;
//
//	private String getFrontendBaseUrl(HttpServletRequest request) {
//		String origin = request.getHeader("Origin");
//		String referer = request.getHeader("Referer");
//
//		if ((origin != null && origin.contains("localhost")) || (referer != null && referer.contains("localhost"))) {
//			return localFrontendUrl;
//		}
//		return prodFrontendUrl;
//	}
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		if (!(authentication instanceof OAuth2AuthenticationToken)) {
//			super.onAuthenticationSuccess(request, response, authentication);
//			return;
//		}
//
//		// Get the intended redirect URI from the cookie (if set by frontend)
//		String redirectUri = CookieUtils
//				.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
//				.map(Cookie::getValue).orElse(null);
//
//		// Use default frontend URL if no specific redirect was requested
//		if (redirectUri == null || !isAuthorizedRedirectUri(redirectUri)) {
//			redirectUri = getFrontendBaseUrl(request);
//		}
//
//		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//		OAuth2User oauth2User = oauthToken.getPrincipal();
//		Map<String, Object> attributes = oauth2User.getAttributes();
//
//		// Extract user details safely
//		String email = getAttributeAsString(attributes, "email");
//		String name = getAttributeAsString(attributes, "name");
//		boolean requiresProfileCompletion = getAttributeAsBoolean(attributes, "requiresProfileCompletion", false);
//
//		if (email == null || email.isEmpty()) {
//			// Handle missing email case
//			getRedirectStrategy().sendRedirect(request, response, UriComponentsBuilder.fromUriString(redirectUri)
//					.path("/oauth-error").queryParam("reason", "missing-email").build().toUriString());
//			return;
//		}
//
//		// Generate JWT for the authenticated user
//		String token = jwtService.generateToken(email);
//
//		// Build the final redirect URL based on whether profile completion is needed
//		String finalRedirectUrl;
//		if (requiresProfileCompletion) {
//			finalRedirectUrl = UriComponentsBuilder.fromUriString(redirectUri).path("/complete-profile")
//					.queryParam("token", token).queryParam("email", email).queryParam("name", name).build()
//					.toUriString();
//		} else {
//			finalRedirectUrl = UriComponentsBuilder.fromUriString(redirectUri).path("/oauth-callback")
//					.queryParam("token", token).build().toUriString();
//		}
//
//		// Clear auth request cookies
//		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//
//		// Redirect to frontend with token
//		getRedirectStrategy().sendRedirect(request, response, finalRedirectUrl);
//	}
//
//	// Helper methods for safer attribute access
//	private String getAttributeAsString(Map<String, Object> attributes, String key) {
//		Object value = attributes.get(key);
//		return value instanceof String ? (String) value : null;
//	}
//
//	private boolean getAttributeAsBoolean(Map<String, Object> attributes, String key, boolean defaultValue) {
//		Object value = attributes.get(key);
//		return value instanceof Boolean ? (Boolean) value : defaultValue;
//	}
//
//	// Validate that the redirect URI is one that we allow
//	private boolean isAuthorizedRedirectUri(String uri) {
//		// Add logic to validate the redirect URI against a list of allowed URIs
//		// This is important for security to prevent open redirects
//		return uri != null && (uri.startsWith(localFrontendUrl) || uri.startsWith(prodFrontendUrl));
//	}
//}

//package com.flight.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.flight.management.configuration.HttpCookieOAuth2AuthorizationRequestRepository;
import com.flight.management.util.JwtService;

//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

//@Component
//public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//	@Autowired
//	private JwtService jwtService;
//
//	@Value("${app.frontend.url.local}")
//	private String localFrontendUrl;
//
//	@Value("${app.frontend.url.prod}")
//	private String prodFrontendUrl;
//
//	@Autowired
//	private HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;
//
//	// Helper method to determine frontend URL based on request origin
//	private String getFrontendBaseUrl(HttpServletRequest request) {
////		String origin = request.getHeader("Origin");
////		String referer = request.getHeader("Referer");
////
////		System.err.println("Origin: " + origin);
////		System.err.println("Referer: " + referer);
////		
////		
////		if ((origin != null && origin.contains("localhost")) || (referer != null && referer.contains("localhost"))) {
////			return localFrontendUrl;
////		}
////		return prodFrontendUrl;
//
//		String state = request.getParameter("state");
//		System.err.println("State: " + state);
//		if ("local".equals(state)) {
//			return localFrontendUrl;
//		}
//		return prodFrontendUrl;
//	}
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		if (!(authentication instanceof OAuth2AuthenticationToken)) {
//			super.onAuthenticationSuccess(request, response, authentication);
//			return;
//		}
//
//		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//		OAuth2User oauth2User = oauthToken.getPrincipal();
//		Map<String, Object> attributes = oauth2User.getAttributes();
//
//		// Extract user information
//		String email = getAttributeAsString(attributes, "email");
//		String name = getAttributeAsString(attributes, "name");
//
//		// Check if email exists
//		if (email == null || email.isEmpty()) {
//			handleAuthenticationFailure(request, response, "Missing email information");
//			return;
//		}
//
//		// Generate JWT token
//		String token = jwtService.generateToken(email);
//
//		// Check if user profile needs completion (this could be determined by your user
//		// service)
//		boolean requiresProfileCompletion = determineIfProfileCompletionRequired(email);
//
//		// Get frontend URL for redirection
//		String frontendBaseUrl = getFrontendBaseUrl(request);
//
//		// Build redirect URL with token
//		String redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/complete-profile")
//				.queryParam("token", token).queryParam("requiresProfileCompletion", requiresProfileCompletion)
//				.queryParam("email", email).queryParam("name", name).build().toUriString();
//
//		// Clean up cookies
//		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//
//		// Redirect to frontend
//		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//	}
//
//	private void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, String reason)
//			throws IOException {
//		String frontendBaseUrl = getFrontendBaseUrl(request);
//		String redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/oauth-callback")
//				.queryParam("error", "authentication_failure").queryParam("error_description", reason).build()
//				.toUriString();
//
//		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//	}
//
//	// Helper method to check if user profile needs completion
//	private boolean determineIfProfileCompletionRequired(String email) {
//		// Implementation depends on your user service
//		// Example:
//		// return userService.isProfileIncomplete(email);
//
//		// For now, returning a placeholder
//		return false; // Change this based on your actual implementation
//	}
//
//	// Helper methods for safer attribute access
//	private String getAttributeAsString(Map<String, Object> attributes, String key) {
//		Object value = attributes.get(key);
//		return value instanceof String ? (String) value : null;
//	}
//}

//@Component
//public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//	@Autowired
//	private JwtService jwtService;
//
//	@Value("${app.frontend.url.local}")
//	private String localFrontendUrl;
//
//	@Value("${app.frontend.url.prod}")
//	private String prodFrontendUrl;
//
//	@Autowired
//	private HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;
//
//	// Improved method to determine frontend URL based on request state
//	private String getFrontendBaseUrl(HttpServletRequest request) {
//		// First check the state parameter from OAuth flow
//		String state = request.getParameter("state");
////		System.err.println("State parameter: " + state);
//
//		// Also check for env parameter in the request attributes
//		// (This would be set by our CustomAuthorizationRequestResolver)
//		Object envAttribute = request.getAttribute("env");
//		String env = envAttribute != null ? envAttribute.toString() : null;
////		System.err.println("Env attribute: " + env);
//
//		// Use state parameter first, then fall back to env attribute
//		String environment = state != null ? state : env;
//
//		// If we have a valid "local" environment indicator, use local URL
//		if ("local".equals(environment)) {
////			System.err.println("Redirecting to local frontend: " + localFrontendUrl);
//			return localFrontendUrl;
//		}
//
//		// Default to production URL
////		System.err.println("Redirecting to production frontend: " + prodFrontendUrl);
//		return prodFrontendUrl;
//	}
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		if (!(authentication instanceof OAuth2AuthenticationToken)) {
//			super.onAuthenticationSuccess(request, response, authentication);
//			return;
//		}
//
//		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//		OAuth2User oauth2User = oauthToken.getPrincipal();
//		Map<String, Object> attributes = oauth2User.getAttributes();
//
//		// Extract user information
//		String email = getAttributeAsString(attributes, "email");
//		String name = getAttributeAsString(attributes, "name");
//
//		// Log the attributes for debugging
////		System.err.println("OAuth2 User Attributes: " + attributes);
//
//		// Check if email exists
//		if (email == null || email.isEmpty()) {
//			handleAuthenticationFailure(request, response, "Missing email information");
//			return;
//		}
//
//		// Generate JWT token
//		String token = jwtService.generateToken(email);
//
//		// Check if user profile needs completion
//		boolean requiresProfileCompletion = determineIfProfileCompletionRequired(email);
//
//		// Get frontend URL for redirection
//		String frontendBaseUrl = getFrontendBaseUrl(request);
//
//		// Build redirect URL with token
//		String redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/complete-profile")
//				.queryParam("token", token).queryParam("requiresProfileCompletion", requiresProfileCompletion)
//				.queryParam("email", email).queryParam("name", name).build().toUriString();
//
////		System.err.println("Final redirect URL: " + redirectUrl);
//
//		// Clean up cookies
//		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//
//		// Redirect to frontend
//		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//	}
//
//	private void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, String reason)
//			throws IOException {
//		String frontendBaseUrl = getFrontendBaseUrl(request);
//		String redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/oauth-callback")
//				.queryParam("error", "authentication_failure").queryParam("error_description", reason).build()
//				.toUriString();
//
//		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//	}
//
//	// Helper method to check if user profile needs completion
//	private boolean determineIfProfileCompletionRequired(String email) {
//		// Implementation depends on your user service
//		// For now, returning a placeholder
//		return false; // Change this based on your actual implementation
//	}
//
//	// Helper methods for safer attribute access
//	private String getAttributeAsString(Map<String, Object> attributes, String key) {
//		Object value = attributes.get(key);
//		return value instanceof String ? (String) value : null;
//	}
//}

@Component
public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	private JwtService jwtService;

	@Value("${app.frontend.url.local}")
	private String localFrontendUrl;

	@Value("${app.frontend.url.prod}")
	private String prodFrontendUrl;

	@Autowired
	private UserRepo repo; // Repository for checking if user exists

	@Autowired
	private HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;

	// Improved method to determine frontend URL based on request state
	private String getFrontendBaseUrl(HttpServletRequest request) {
		// First check the state parameter from OAuth flow
		String state = request.getParameter("state");

		// Also check for env parameter in the request attributes
		// (This would be set by our CustomAuthorizationRequestResolver)
		Object envAttribute = request.getAttribute("env");
		String env = envAttribute != null ? envAttribute.toString() : null;

		// Use state parameter first, then fall back to env attribute
		String environment = state != null ? state : env;

		// If we have a valid "local" environment indicator, use local URL
		if ("local".equals(environment)) {
			return localFrontendUrl;
		}

		// Default to production URL
		return prodFrontendUrl;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if (!(authentication instanceof OAuth2AuthenticationToken)) {
			super.onAuthenticationSuccess(request, response, authentication);
			return;
		}

		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		OAuth2User oauth2User = oauthToken.getPrincipal();
		Map<String, Object> attributes = oauth2User.getAttributes();

		// Extract user information
		String email = getAttributeAsString(attributes, "email");
		String name = getAttributeAsString(attributes, "name");

		// Check if email exists
		if (email == null || email.isEmpty()) {
			handleAuthenticationFailure(request, response, "Missing email information");
			return;
		}

		// Generate JWT token
		String token=jwtService.generateToken(email);
		
//		Optional<UserEntity> user = repo.findByEmailId(email);
//		
//		String token=null;
//		
//		if(user.isPresent())
//		token = jwtService.generateToken(user.get().getUsername());

		// Check if user profile needs completion
		boolean requiresProfileCompletion = determineIfProfileCompletionRequired(email);

		// Get frontend URL for redirection
		String frontendBaseUrl = getFrontendBaseUrl(request);
		String redirectUrl;

		// Create a secure HTTP-only cookie with the JWT token
		Cookie tokenCookie = new Cookie("auth_token", token);
		tokenCookie.setHttpOnly(true);
		tokenCookie.setSecure(request.isSecure());
		tokenCookie.setPath("/");
		tokenCookie.setMaxAge(60 * 24 * 60 * 60 * 1000); // 2 months in seconds
		response.addCookie(tokenCookie);

		// Handle differently based on whether user exists or not
		if (requiresProfileCompletion) {
			// For new users, redirect to profile completion with query parameters
			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/complete-profile")
					.queryParam("token", token) // Also include token in URL for Angular to detect
					.queryParam("requiresProfileCompletion", true).queryParam("email", email).queryParam("name", name)
					.build().toUriString();
		} else {
			// For existing users, redirect to home with token in URL for Angular to detect
			// This will allow the navbar to update immediately
			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/").queryParam("token", token)
					.queryParam("role", getUserRole(email)).build().toUriString();
		}

		// Clean up cookies
		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

		// Redirect to frontend
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}

	// Helper method to get user role
	private String getUserRole(String email) {
		// Get user from repository and return role
		return repo.findByEmailId(email).map(user -> user.getRole()).orElse("USER"); // Default to USER if not found
	}

	private void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, String reason)
			throws IOException {
		String frontendBaseUrl = getFrontendBaseUrl(request);
		String redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/oauth-callback")
				.queryParam("error", "authentication_failure").queryParam("error_description", reason).build()
				.toUriString();

		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}

	// Updated method to actually check if user profile needs completion
	private boolean determineIfProfileCompletionRequired(String email) {
		// Check if user exists in the database
		return repo.findByEmailId(email).isEmpty();
	}

	// Helper methods for safer attribute access
	private String getAttributeAsString(Map<String, Object> attributes, String key) {
		Object value = attributes.get(key);
		return value instanceof String ? (String) value : null;
	}
}