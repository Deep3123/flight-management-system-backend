package com.flight.management.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.flight.management.domain.UserEntity;
import com.flight.management.proxy.LoginReq;
import com.flight.management.proxy.LoginResp;
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
	public String deleteUserByUsernmae(String username) {
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

}
