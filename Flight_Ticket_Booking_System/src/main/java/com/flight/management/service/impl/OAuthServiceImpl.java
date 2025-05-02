package com.flight.management.service.impl;

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

		if (user.getPassword() == null) {
			String secureRandomPassword = generateSecurePassword();
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

	private String generateSecurePassword() {
		return UUID.randomUUID().toString().substring(0, 12);
	}
}
