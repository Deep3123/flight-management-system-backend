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
import com.flight.management.service.OAuthService;
import com.flight.management.util.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

	@Autowired
	private OAuthService oAuthService;

	@PostMapping("/complete-profile")
	public ResponseEntity<?> completeProfile(@Valid @RequestBody OAuthProfileCompletion profileData,
			@RequestHeader("Authorization") String authHeader, BindingResult bindingResult) {
//		System.err.println("Request is at Oauth controller.");
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());
			return new ResponseEntity<>(new Response(errors.toString(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		try {
			String token = authHeader.substring(7);
			LoginResp response = oAuthService.completeProfile(profileData, token);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new Response(e.getMessage(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new Response("Error completing profile: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
