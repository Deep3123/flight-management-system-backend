package com.flight.management.controller;

import com.github.cage.Cage;
import com.github.cage.GCage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@RestController
//public class CaptchaController {
//
//	private final Cage cage = new GCage();
//
//	@GetMapping(value = "/captcha", produces = MediaType.IMAGE_PNG_VALUE)
//	public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
//		String captchaToken = cage.getTokenGenerator().next();
//		captchaToken = captchaToken.substring(0, 6);
//		// Store CAPTCHA token in session
//		session.setAttribute("captcha", captchaToken);
//		System.err.println(captchaToken);
//		
//
//		response.setContentType("image/png");
//		cage.draw(captchaToken, response.getOutputStream());
//	}
//}

@RestController
public class CaptchaController {
	private final Cage cage = new GCage();
	private final Map<String, String> captchaStore = new ConcurrentHashMap<>(); // Store captchas with tokens

	@GetMapping(value = "/captcha", produces = MediaType.IMAGE_PNG_VALUE)
	public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
		String captchaToken = cage.getTokenGenerator().next();
		captchaToken = captchaToken.substring(0, 6);

		// Generate a unique session identifier
		String sessionId = session.getId();

		// Store CAPTCHA with the session ID
		captchaStore.put(sessionId, captchaToken);
		System.err.println("Stored captcha: " + captchaToken + " for session: " + sessionId);

		// Set the session ID in the response header
		response.setHeader("X-Auth-Token", sessionId);

		response.setContentType("image/png");
		cage.draw(captchaToken, response.getOutputStream());
	}

	// Method to validate captcha (can be called from your login controller)
	public boolean validateCaptcha(String sessionId, String userInput) {
		if (sessionId == null || userInput == null)
			return false;

		String storedCaptcha = captchaStore.get(sessionId);
		System.err.println(
				"Validating - Session: " + sessionId + ", User Input: " + userInput + ", Stored: " + storedCaptcha);

		if (storedCaptcha != null && storedCaptcha.equalsIgnoreCase(userInput)) {
			captchaStore.remove(sessionId); // Use once only
			return true;
		}
		return false;
	}
}