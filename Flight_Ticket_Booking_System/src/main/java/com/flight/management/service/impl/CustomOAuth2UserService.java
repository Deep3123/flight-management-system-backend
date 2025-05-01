package com.flight.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.flight.management.domain.UserEntity;
import com.flight.management.repo.UserRepo;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);

		// Extract provider-specific details
		Map<String, Object> attributes = oauth2User.getAttributes();
		String email = (String) attributes.get("email");
		String name = (String) attributes.get("name");

		String provider = userRequest.getClientRegistration().getRegistrationId();
		String providerId = (String) attributes.get("sub"); // For Google - use appropriate field for other providers

		// Check if user exists in our database
		Optional<UserEntity> existingUser = userRepo.findByEmailId(email);
		UserEntity user;

		// Store OAuth details for session management
		Map<String, Object> customAttributes = new HashMap<>(attributes);
		customAttributes.put("provider", provider);
		customAttributes.put("providerId", providerId);

		if (existingUser.isEmpty()) {
			// New user - mark for profile completion
			customAttributes.put("requiresProfileCompletion", true);

			// Create a temporary OAuth user record (not persisted yet)
			customAttributes.put("tempEmail", email);
			customAttributes.put("tempName", name);
		} else {
			user = existingUser.get();
			// Existing user - check if profile is complete
			if (user.getUsername() == null || user.getMobileNo() == null) {
				customAttributes.put("requiresProfileCompletion", true);
			} else {
				customAttributes.put("requiresProfileCompletion", false);
			}
			customAttributes.put("userId", user.getId());
		}

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new DefaultOAuth2User(authorities, customAttributes, "email");
	}
}
