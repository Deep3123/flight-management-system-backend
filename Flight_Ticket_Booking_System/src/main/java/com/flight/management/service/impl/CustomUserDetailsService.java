package com.flight.management.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.flight.management.domain.UserEntity;
import com.flight.management.repo.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo repo;

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		Optional<UserEntity> user = repo.findByUsername(username);
//
//		if (user.isEmpty())
//			throw new UsernameNotFoundException("User not found with given username, please verify the username!!");
//
//		return new CustomUserDetails(user.get());
//	}

//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		Optional<UserEntity> user = repo.findByEmailId(email);
//
//		if (user.isEmpty())
//			throw new UsernameNotFoundException("User not found with given username, please verify the username!!");
//
//		return new CustomUserDetails(user.get());
//	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// Try to find by email first (for OAuth flow)
		Optional<UserEntity> user = repo.findByEmailId(usernameOrEmail);

		// If not found by email, try by username (for regular login)
		if (user.isEmpty()) {
			user = repo.findByUsername(usernameOrEmail);
		}

		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User not found with given identifier: " + usernameOrEmail);
		}

		// Return CustomUserDetails with email as username
		return new CustomUserDetails(user.get());
	}
}
