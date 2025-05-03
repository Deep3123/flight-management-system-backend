package com.flight.management.util;

import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Base64;
import java.util.Optional;

public class CookieUtils {
	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return Optional.of(cookie);
				}
			}
		}

		return Optional.empty();
	}

	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxAge);

		// Only set these for production environments
		if (!isLocalEnvironment()) {
			cookie.setSecure(true);
			cookie.setAttribute("SameSite", "None");
		}

		response.addCookie(cookie);
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);

					// Only set these for production environments
					if (!isLocalEnvironment()) {
						cookie.setSecure(true);
						cookie.setAttribute("SameSite", "None");
					}

					response.addCookie(cookie);
					break;
				}
			}
		}
	}

	public static String serialize(Object object) {
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
	}

	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
	}

	// Helper method to determine if we're in local development environment
	private static boolean isLocalEnvironment() {
		// You can customize this logic based on your environment setup
		// For example, check for a system property or environment variable
		String environment = System.getProperty("spring.profiles.active");
		return environment != null && environment.contains("local");
	}
}
