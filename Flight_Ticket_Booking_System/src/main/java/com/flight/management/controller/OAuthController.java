package com.flight.management.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.domain.UserEntity;
import com.flight.management.proxy.LoginResp;
import com.flight.management.proxy.OAuthProfileCompletion;
import com.flight.management.proxy.Response;
import com.flight.management.repo.UserRepo;
import com.flight.management.util.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder encoder;

	@PostMapping("/complete-profile")
	public ResponseEntity<?> completeProfile(@Valid @RequestBody OAuthProfileCompletion profileData,
			@RequestHeader("Authorization") String authHeader, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			return new ResponseEntity<>(new Response(errors.toString(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		try {
			// Validate token
			String token = authHeader.substring(7); // Remove "Bearer " prefix
			String email = jwtService.extractUserName(token);

			if (!email.equals(profileData.getEmail())) {
				return new ResponseEntity<>(
						new Response("Email mismatch with token", HttpStatus.BAD_REQUEST.toString()),
						HttpStatus.BAD_REQUEST);
			}

			// Check if username already exists
			if (userRepo.findByUsername(profileData.getUsername()).isPresent()) {
				return new ResponseEntity<>(new Response("Username already exists", HttpStatus.BAD_REQUEST.toString()),
						HttpStatus.BAD_REQUEST);
			}

			// Find or create user
			UserEntity user = userRepo.findByEmailId(email).orElse(new UserEntity());

			// Update user data
			user.setUsername(profileData.getUsername());
			user.setMobileNo(profileData.getMobileNo());
			user.setEmailId(email);
			user.setName(profileData.getName());
			user.setRole("USER");

			// For OAuth users, set a random secure password they can reset later
			if (user.getPassword() == null) {
				String secureRandomPassword = generateSecurePassword();
				user.setPassword(encoder.encode(secureRandomPassword));
			}

			// Set timestamps
			Date now = new Date();
			if (user.getCreatedAt() == null) {
				user.setCreatedAt(now);
			}
			user.setUpdatedAt(now);

			// Save user
			userRepo.save(user);

			// Generate a fresh token with the username
			String newToken = jwtService.generateToken(profileData.getUsername());

			// Return successful response with token
			LoginResp response = new LoginResp(profileData.getUsername(), newToken, "USER");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(new Response("Error completing profile: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String generateSecurePassword() {
		// Generate a secure random password (this is just a placeholder)
		return java.util.UUID.randomUUID().toString().substring(0, 12);
	}
}
