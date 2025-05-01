package com.flight.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.flight.management.util.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private JwtService jwtService;

	private final String frontendBaseUrl = "https://jetwayz.vercel.app"; // Change this to your frontend URL

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		OAuth2User oauth2User = oauthToken.getPrincipal();
		Map<String, Object> attributes = oauth2User.getAttributes();

		// Get email as username - will serve as the principal identifier
		String email = (String) attributes.get("email");

		// Check if user needs to complete their profile
		boolean requiresProfileCompletion = attributes.containsKey("requiresProfileCompletion")
				? (boolean) attributes.get("requiresProfileCompletion")
				: false;

		// Generate JWT token
		String token = jwtService.generateToken(email);

		// Redirect to appropriate page
		String redirectUrl;
		if (requiresProfileCompletion) {
			// Redirect to profile completion form
			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/complete-profile")
					.queryParam("token", token).queryParam("email", email).queryParam("name", attributes.get("name"))
					.build().toUriString();
		} else {
			// Redirect to home page with token
			redirectUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl + "/oauth-callback")
					.queryParam("token", token).build().toUriString();
		}

		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}
