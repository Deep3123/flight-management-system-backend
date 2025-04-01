package com.flight.management.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.LoginReq;
import com.flight.management.proxy.LoginResp;
import com.flight.management.proxy.ResetPassword;
import com.flight.management.proxy.Response;
import com.flight.management.proxy.UserProxy;
import com.flight.management.service.UserService;

import jakarta.validation.Valid;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;

	@PostMapping("/register")
	public ResponseEntity<?> saveUserDetails(@Valid @RequestBody UserProxy userProxy, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Extract error messages
			List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			return new ResponseEntity<>(new Response(errors.toString(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		String result = service.saveUserDetails(userProxy);
		if (result.contains("User already exist")) {
			return new ResponseEntity<>(new Response(result, HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(new Response(result, HttpStatus.CREATED.toString()), HttpStatus.CREATED);
	}

	@GetMapping("/get-all-user-details")
	public ResponseEntity<?> getAllUsersDetails() {
//		System.err.println("Control is here.");
		List<UserProxy> list = service.getAllUsersDetails();

		if (list != null && !list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);

		else
			return new ResponseEntity<>(
					new Response("No data found to display currently!!", HttpStatus.NOT_FOUND.toString()),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/get-user-by-username/{username}")
	public ResponseEntity<?> getUserByUsername(@Valid @PathVariable("username") String username) {
		UserProxy user = service.getUserByUsername(username);

		if (user != null)
			return new ResponseEntity<>(user, HttpStatus.OK);

		else
			return new ResponseEntity<>(new Response("User not found with given username, please verify the username!!",
					HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);

	}

	@PostMapping("/update-user-by-username")
	public ResponseEntity<?> updateUserByUsername(@Valid @RequestBody UserProxy userProxy,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Extract error messages
			List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			return new ResponseEntity<>(new Response(errors.toString(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		String s = service.updateUserByUsername(userProxy);

		if (s != null && !s.isEmpty())
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		else
			return new ResponseEntity<>(new Response("User not found with given username, please verify the username!!",
					HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);

	}

	@GetMapping("/delete-user-by-username/{username}")
	public ResponseEntity<?> deleteUserByUsernmae(@Valid @PathVariable("username") String username) {
		String s = service.deleteUserByUsername(username);

		if (s != null && !s.isEmpty())
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		else
			return new ResponseEntity<>(new Response("User not found with given username, please verify the username!!",
					HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginReq req) {
		try {
			LoginResp res = service.login(req);
//			System.err.println(req.getUsername());
//			System.err.println(req.getPassword());
			return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(new Response(e.getMessage(), HttpStatus.UNAUTHORIZED.toString()),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody String email) {
		String s = service.forgotPassword(email);

		if (s.equals("A password reset link has been sent to your registered email address. "
				+ "Please check your inbox and follow the instructions to reset your password."))
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		return new ResponseEntity<>(new Response(s, HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/reset-password/{username}/{timestamp}/{token}")
	public ResponseEntity<?> resetPassword(@PathVariable("username") String username,
			@PathVariable("timestamp") String timestamp, @PathVariable("token") String token,
			@RequestBody ResetPassword proxy) {
		try {
			// Pass username and token to service for processing
			String s = service.resetPassword(username, timestamp, token, proxy);

			if (s.equals("Password not matching.") || s.equals("Username in token does not match provided username!")
					|| s.equals("Token is expired, please request again to reset your password!")
					|| s.equals("User was not found to perform this action!")) {
				return new ResponseEntity<>(new Response(s, HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new Response("Invalid token format.", HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}
	}

}
