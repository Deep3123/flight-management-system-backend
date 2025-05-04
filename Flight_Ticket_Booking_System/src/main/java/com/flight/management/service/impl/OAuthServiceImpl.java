package com.flight.management.service.impl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flight.management.domain.UserEntity;
import com.flight.management.proxy.LoginResp;
import com.flight.management.proxy.OAuthProfileCompletion;
import com.flight.management.repo.UserRepo;
import com.flight.management.service.OAuthService;
import com.flight.management.util.JwtService;

@Service
public class OAuthServiceImpl implements OAuthService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder encoder;

	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL = "@$!%*?&";

	private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SPECIAL;
	private static final SecureRandom random = new SecureRandom();

	@Override
	public LoginResp completeProfile(OAuthProfileCompletion profileData, String token) throws Exception {
		String email = jwtService.extractUserName(token);

		if (!email.equals(profileData.getEmail())) {
			throw new IllegalArgumentException("Email mismatch with token");
		}

		if (userRepo.findByUsername(profileData.getUsername()).isPresent()) {
			throw new IllegalArgumentException("Username already exists");
		}

		UserEntity user = userRepo.findByEmailId(email).orElse(new UserEntity());

		user.setUsername(profileData.getUsername());
		user.setMobileNo(profileData.getMobileNo());
		user.setEmailId(email);
		user.setName(profileData.getName());
		user.setRole("USER");

//		if (user.getPassword() != null)
//			System.err.println("Old password if available: " + user.getPassword());

		if (user.getPassword() == null) {
			String secureRandomPassword = generateSecurePassword(8);
//			System.err.println("Generated Password: " + secureRandomPassword);
			user.setPassword(encoder.encode(secureRandomPassword));
		}

		Date now = new Date();
		if (user.getCreatedAt() == null) {
			user.setCreatedAt(now);
		}
		user.setUpdatedAt(now);

		userRepo.save(user);

		String newToken = jwtService.generateToken(profileData.getUsername());
		return new LoginResp(profileData.getUsername(), newToken, "USER");
	}

//	private String generateSecurePassword() {
//		return UUID.randomUUID().toString().substring(0, 12);
//	}

	public static String generateSecurePassword(int length) {
		if (length < 8) {
			throw new IllegalArgumentException("Password must be at least 8 characters long.");
		}

		StringBuilder password = new StringBuilder();

		// Ensure each required character type is present
		password.append(UPPER.charAt(random.nextInt(UPPER.length())));
		password.append(LOWER.charAt(random.nextInt(LOWER.length())));
		password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
		password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

		// Fill the rest with random characters from all categories
		for (int i = 4; i < length; i++) {
			password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
		}

		// Shuffle the characters to avoid predictable order
		return shuffleString(password.toString());
	}

	private static String shuffleString(String input) {
		char[] characters = input.toCharArray();
		for (int i = characters.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			// Simple swap
			char temp = characters[i];
			characters[i] = characters[index];
			characters[index] = temp;
		}
		return new String(characters);
	}
}
