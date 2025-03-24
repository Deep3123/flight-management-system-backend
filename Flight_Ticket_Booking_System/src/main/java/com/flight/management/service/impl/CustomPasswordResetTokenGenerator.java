package com.flight.management.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.flight.management.domain.UserEntity;

@Component
public class CustomPasswordResetTokenGenerator {

	private static final String SECRET_KEY = "dowmjmuiapmxlzpmjcio85a8aa9t49h7w9kjsakkcmsoopkmski7kiskskahjdk";

	// Method to generate token
	public String generateToken(UserEntity user) {

		String data = user.getUsername() + ":" + user.getPassword(); // Separate username, password,
																		// and timestamp with a colon

		// Generate the HMAC SHA-256 token and encode it to Base64
		String hmacToken = generateHmacSha256Token(data);
		// Instead of directly returning the token, we will Base64 encode it here
		return Base64.getEncoder().encodeToString(hmacToken.getBytes(StandardCharsets.UTF_8));
	}

	// Method to validate token
	public boolean validateToken(Long tokenTimestamp, String token, UserEntity user) {
		Long expirationTime = System.currentTimeMillis() / 1000 - (10 * 60); // Token valid for 10 minutes

		if (tokenTimestamp < expirationTime) {
			return false;
		}

		String expectedToken = generateToken(user);

		return token.equals(expectedToken);
	}

	private String generateHmacSha256Token(String data) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
			mac.init(secretKeySpec);
			byte[] bytes = mac.doFinal(data.getBytes());
			return new String(bytes); // Return the raw byte output (later encoded in Base64)
		} catch (Exception e) {
			throw new RuntimeException("Error generating token", e);
		}
	}

//	// Method to extract timestamp from token
//	private long getTimestampFromToken(String token) {
//		try {
//			// Decode the token from Base64
//			String decoded = new String(Base64.getDecoder().decode(token));
//			// Extract the timestamp (the third part of the token after splitting by ':')
//			return Long.parseLong(decoded.split(":")[2]);
//		} catch (Exception e) {
//			throw new RuntimeException("Error extracting timestamp from token", e);
//		}
//	}
//
//	// Method to extract username from token
//	public String getUsernameFromToken(String token) {
//		try {
//			// Decode the token from Base64 (this should be Base64 decoded string)
//			String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
//
//			// Print out the decoded token for debugging purposes
//			System.out.println("Decoded Token: " + decoded);
//
//			// Extract the username (the first part of the token before the first colon)
//			return decoded.split(":")[0];
//		} catch (Exception e) {
//			throw new RuntimeException("Error extracting username from token", e);
//		}
//	}

}
