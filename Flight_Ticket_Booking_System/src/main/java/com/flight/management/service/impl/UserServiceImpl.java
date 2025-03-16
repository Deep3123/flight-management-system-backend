package com.flight.management.service.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flight.management.domain.UserEntity;
import com.flight.management.proxy.LoginReq;
import com.flight.management.proxy.LoginResp;
import com.flight.management.proxy.ResetPassword;
import com.flight.management.proxy.UserProxy;
import com.flight.management.repo.UserRepo;
import com.flight.management.service.UserService;
import com.flight.management.util.JwtService;
import com.flight.management.util.MapperUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo repo;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Autowired
	private CustomPasswordResetTokenGenerator generator;

	@Override
	public String saveUserDetails(UserProxy userProxy) {
		// TODO Auto-generated method stub
		if (repo.findByUsername(userProxy.getUsername()).isEmpty()
				&& repo.findByEmailId(userProxy.getEmailId()).isEmpty()) {
			userProxy.setPassword(encoder.encode(userProxy.getPassword()));
			repo.save(MapperUtil.convertValue(userProxy, UserEntity.class));
		}

		else if (repo.findByUsername(userProxy.getUsername()).isPresent())
			return "User already exist with given username.";

		else if (repo.findByEmailId(userProxy.getEmailId()).isPresent())
			return "User already exist with given email-id.";

		return "Data saved successfully.";
	}

	@Override
	public List<UserProxy> getAllUsersDetails() {
		// TODO Auto-generated method stub
		return MapperUtil.convertListofValue(repo.findAll(), UserProxy.class);
	}

	@Override
	public UserProxy getUserByUsername(String username) {
		// TODO Auto-generated method stub
		Optional<UserEntity> user = repo.findByUsername(username);

		if (user.isPresent())
			return MapperUtil.convertValue(user.get(), UserProxy.class);

		else
			return null;
	}

	@Override
	public String updateUserByUsername(UserProxy userProxy) {
		// TODO Auto-generated method stub
		Optional<UserEntity> user = repo.findByUsername(userProxy.getUsername());

		if (user.isPresent()) {
			if (userProxy.getName() != null)
				user.get().setName(userProxy.getName());

			if (userProxy.getEmailId() != null)
				user.get().setEmailId(userProxy.getEmailId());

			if (userProxy.getMobileNo() != null)
				user.get().setMobileNo(userProxy.getMobileNo());

			if (userProxy.getPassword() != null)
				user.get().setPassword(encoder.encode(userProxy.getPassword()));
//				user.get().setPassword(userProxy.getPassword());

			repo.save(user.get());

			return "Data updated successfully.";
		}

		else
			return null;

	}

	@Override
	public String deleteUserByUsername(String username) {
		// TODO Auto-generated method stub
		Optional<UserEntity> user = repo.findByUsername(username);

		if (user.isPresent()) {
			repo.delete(user.get());
			return "User deleted successfully.";
		}

		else
			return null;
	}

	@Override
	public LoginResp login(LoginReq req) {
		// TODO Auto-generated method stub
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(req.getUsername(),
				req.getPassword());

		Authentication authenticate = manager.authenticate(auth);

		if (authenticate.isAuthenticated()) {
			return new LoginResp(req.getUsername(), jwtService.generateToken(req.getUsername()));
		}
		return null;
	}

	@Override
	public String forgotPassword(String email) {
		Optional<UserEntity> user = repo.findByEmailId(email);

		if (user.isPresent()) {
			String token = generator.generateToken(user.get());

			Long time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
			String timestamp = Base64.getEncoder().encodeToString(time.toString().getBytes());

			// Base64 encode the token
			String encodedToken = Base64.getEncoder().encodeToString(token.getBytes());

			String username = Base64.getEncoder().encodeToString(user.get().getUsername().getBytes());

			String url = "http://localhost:8080/user/reset-password/" + username + "/" + timestamp + "/" + encodedToken;
			System.err.println(url);

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(sender);
			mailMessage.setTo(email);
			mailMessage.setSubject("Password Reset Request");
			mailMessage.setText("Reset your password for flight ticket booking system: " + url);

			javaMailSender.send(mailMessage);

			return "Password reset email sent.";
		}
		return "User with the provided email not found.";
	}

	@Override
	public String resetPassword(String username, String timestamp, String token, ResetPassword proxy) {
		if (!proxy.getPassword().equals(proxy.getConfirmPassword())) {
			return "Password not matching.";
		}

		// Decode the token at service layer
		String decodedToken = decodeToken(token);

		// Extract the username from the decoded token
		String tokenUsername = new String(Base64.getDecoder().decode(username), StandardCharsets.UTF_8);

		//
		Long time = Long.parseLong(new String(Base64.getDecoder().decode(timestamp)));

		// Now validate the token
		Optional<UserEntity> user = repo.findByUsername(tokenUsername);

		if (user.isPresent()) {
			if (generator.validateToken(time, decodedToken, user.get())) {
				user.get().setPassword(encoder.encode(proxy.getPassword()));
				repo.save(user.get());
				return "Password was updated successfully.";
			} else {
				return "Token is expired, please request again to reset your password!";
			}
		} else {
			return "User was not found to perform this action!";
		}
	}

	// Helper method to decode the token (used inside the service)
	private String decodeToken(String token) {
		try {
			// Decode the Base64-encoded token
			return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid token format.", e);
		}
	}

}