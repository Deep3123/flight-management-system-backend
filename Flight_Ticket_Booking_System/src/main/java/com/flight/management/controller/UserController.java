package com.flight.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.LoginReq;
import com.flight.management.proxy.LoginResp;
import com.flight.management.proxy.Response;
import com.flight.management.proxy.UserProxy;
import com.flight.management.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;

	@PostMapping("/register")
	public ResponseEntity<?> saveUserDetails(@Valid @RequestBody UserProxy userProxy) {
		String s = service.saveUserDetails(userProxy);

		if (s.equals("User already exist with given username.") || s.equals("User already exist with given email-id."))
			return new ResponseEntity<>(new Response(s, HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(new Response(s, HttpStatus.CREATED.toString()), HttpStatus.CREATED);
	}

	@GetMapping("/get-all-user-details")
	public ResponseEntity<?> getAllUsersDetails() {
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
	public ResponseEntity<?> updateUserByUsername(@Valid @RequestBody UserProxy userProxy) {
		String s = service.updateUserByUsername(userProxy);

		if (s != null && !s.isEmpty())
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		else
			return new ResponseEntity<>(new Response("User not found with given username, please verify the username!!",
					HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);

	}

	@GetMapping("/delete-user-by-username/{username}")
	public ResponseEntity<?> deleteUserByUsernmae(@Valid @PathVariable("username") String username) {
		String s = service.deleteUserByUsernmae(username);

		if (s != null && !s.isEmpty())
			return new ResponseEntity<>(s, HttpStatus.OK);

		else
			return new ResponseEntity<>(new Response("User not found with given username, please verify the username!!",
					HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginReq req) {
		LoginResp res = service.login(req);

		if (res != null)
			return new ResponseEntity<>(res, HttpStatus.ACCEPTED);

		return new ResponseEntity<>(new Response(
				"User not valid with given username and password, please verify the username and password!!",
				HttpStatus.UNAUTHORIZED.toString()), HttpStatus.UNAUTHORIZED);
	}
}
