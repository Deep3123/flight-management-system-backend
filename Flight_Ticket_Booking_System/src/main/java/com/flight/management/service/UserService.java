package com.flight.management.service;

import java.util.List;
import java.util.Map;

import com.flight.management.proxy.LoginReq;
import com.flight.management.proxy.LoginResp;
import com.flight.management.proxy.ResetPassword;
import com.flight.management.proxy.UserProxy;

public interface UserService {
	public String saveUserDetails(UserProxy userProxy);

	public List<UserProxy> getAllUsersDetails();

	public UserProxy getUserByUsername(String username);

	public String updateUserByUsername(UserProxy userProxy);

	public String deleteUserByUsername(String username);

	public LoginResp login(LoginReq req);

	public String forgotPassword(String email);

	public String resetPassword(String username, String timestamp, String token, ResetPassword proxy);

	public String checkAccountExists(String token);

	public Map<String, Object> getUsersPaginated(int page, int size, String sortField, String sortDirection);

	public long getTotalUsersCount();
}
