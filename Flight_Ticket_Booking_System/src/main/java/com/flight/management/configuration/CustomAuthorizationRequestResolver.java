package com.flight.management.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private final OAuth2AuthorizationRequestResolver defaultResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String baseUri) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, baseUri);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
		return customize(req, request);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
		return customize(req, request);
	}

	private OAuth2AuthorizationRequest customize(OAuth2AuthorizationRequest request, HttpServletRequest httpRequest) {
		if (request == null)
			return null;

		// Get the environment parameter from the request
		String env = httpRequest.getParameter("env");
		if (env == null) {
			env = "prod"; // Default to production if not specified
		}

		// Create a new map of additional parameters that includes all existing ones
		Map<String, Object> additionalParameters = new HashMap<>(request.getAdditionalParameters());
		additionalParameters.put("env", env);

		// Override the state with the environment information
		return OAuth2AuthorizationRequest.from(request).state(env) // Use env as the state directly
				.additionalParameters(additionalParameters).build();
	}
}
