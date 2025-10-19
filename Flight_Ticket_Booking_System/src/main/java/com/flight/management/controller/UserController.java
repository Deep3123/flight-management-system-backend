package com.flight.management.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.LoginReq;
import com.flight.management.proxy.LoginResp;
import com.flight.management.proxy.ResetPassword;
import com.flight.management.proxy.Response;
import com.flight.management.proxy.UserProxy;
import com.flight.management.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;

	@Autowired
	private CaptchaController captchaController;

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

	@GetMapping("/get-users-paginated")
	public ResponseEntity<?> getUsersPaginated(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sortField,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		try {
			Map<String, Object> response = service.getUsersPaginated(page, size, sortField, sortDirection);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Response("Error fetching paginated users: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get-total-users-count")
	public ResponseEntity<?> getTotalUsersCount() {
		try {
			long count = service.getTotalUsersCount();
			return new ResponseEntity<>(count, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Response("Error fetching users count: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
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

//	@PostMapping("/login")
//	public ResponseEntity<?> login(@RequestBody LoginReq req) {
//		try {
//			LoginResp res = service.login(req);
////			System.err.println(req.getUsername());
////			System.err.println(req.getPassword());
//			return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
//		} catch (RuntimeException e) {
//			return new ResponseEntity<>(new Response(e.getMessage(), HttpStatus.UNAUTHORIZED.toString()),
//					HttpStatus.UNAUTHORIZED);
//		}
//	}

//	@PostMapping("/login")
//	public ResponseEntity<?> login(@RequestBody LoginReq req, HttpSession session) {
//		// Step 1: Validate CAPTCHA
//		System.err.println(req.getCaptchaInput());
//		String expectedCaptcha = (String) session.getAttribute("captcha");
//		System.err.println(expectedCaptcha);
//
//		if (expectedCaptcha == null || !expectedCaptcha.equalsIgnoreCase(req.getCaptchaInput())) {
//			return new ResponseEntity<>(
//					new Response("Invalid CAPTCHA. Please try again.", HttpStatus.UNAUTHORIZED.toString()),
//					HttpStatus.UNAUTHORIZED);
//		}
//
//		// Step 2: Proceed with normal login
//		try {
//			LoginResp res = service.login(req);
//			return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
//		} catch (RuntimeException e) {
//			return new ResponseEntity<>(new Response(e.getMessage(), HttpStatus.UNAUTHORIZED.toString()),
//					HttpStatus.UNAUTHORIZED);
//		}
//	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginReq req,
			@RequestHeader(value = "X-Auth-Token", required = false) String sessionToken) {
		// Validate CAPTCHA using the token from header
//		System.err.println("Login with captcha: " + req.getCaptchaInput() + ", token: " + sessionToken);

		if (sessionToken == null || !captchaController.validateCaptcha(sessionToken, req.getCaptchaInput())) {
			return new ResponseEntity<>(
					new Response("Invalid CAPTCHA. Please try again.", HttpStatus.UNAUTHORIZED.toString()),
					HttpStatus.UNAUTHORIZED);
		}

		// Proceed with normal login
		try {
			LoginResp res = service.login(req);
			return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(new Response(e.getMessage(), HttpStatus.UNAUTHORIZED.toString()),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody String email) {
		String s = service.forgotPassword(email);

		if (s.equals("A password reset link has been sent to your registered email address. "
				+ "Please check your inbox and follow the instructions to reset your password."))
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		return new ResponseEntity<>(new Response(s, HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/reset-password/{username}/{timestamp}/{token}")
	public ResponseEntity<?> resetPassword(@Valid @PathVariable("username") String username,
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

	@PostMapping("/check-account-exists")
	public ResponseEntity<?> checkAccountExists(@Valid @RequestBody String token) {
		String s = service.checkAccountExists(token);

		if (s.equals("Success."))
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		return new ResponseEntity<>(new Response(s, HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
	}

    @GetMapping("/download-all-user-data")
    public ResponseEntity<?> downloadAllUserData() {
        Response resp = service.downloadAllUserData();

        if (resp.getStatus_code().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
