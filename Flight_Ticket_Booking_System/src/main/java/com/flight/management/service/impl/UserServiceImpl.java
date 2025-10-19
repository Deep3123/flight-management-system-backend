package com.flight.management.service.impl;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.flight.management.proxy.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flight.management.domain.UserEntity;
import com.flight.management.repo.UserRepo;
import com.flight.management.service.UserService;
import com.flight.management.util.JwtService;
import com.flight.management.util.MapperUtil;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
			userProxy.setCreatedAt(new Date());
			userProxy.setUpdatedAt(new Date());

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
		Boolean flag = false;
		Optional<UserEntity> user = repo.findByUsername(userProxy.getUsername());

		if (user.isPresent()) {
			if (userProxy.getName() != null) {
				if (userProxy.getName() != user.get().getName())
					flag = true;
				user.get().setName(userProxy.getName());
			}

			if (userProxy.getEmailId() != null) {
				if (userProxy.getEmailId() != user.get().getEmailId())
					flag = true;
				user.get().setEmailId(userProxy.getEmailId());
			}

			if (userProxy.getMobileNo() != null) {
				if (userProxy.getMobileNo() != user.get().getMobileNo())
					flag = true;
				user.get().setMobileNo(userProxy.getMobileNo());
			}

            if (userProxy.getRole() != null) {
                if (userProxy.getRole() != user.get().getRole())
                    flag = true;
                user.get().setRole(userProxy.getRole());
            }

//			if (userProxy.getPassword() != null)
//				user.get().setPassword(encoder.encode(userProxy.getPassword()));
////				user.get().setPassword(userProxy.getPassword());

			if (flag == true)
				user.get().setUpdatedAt(new Date());

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

		try {
			Authentication authenticate = manager.authenticate(auth);

			Optional<UserEntity> user = repo.findByUsername(req.getUsername());

			if (authenticate.isAuthenticated() && user.isPresent()) {
//				return new LoginResp(req.getUsername(), jwtService.generateToken(req.getUsername()),
//						user.get().getRole());

				return new LoginResp(req.getUsername(), jwtService.generateToken(user.get().getEmailId()),
						user.get().getRole());
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"The username or password you entered is incorrect. Please verify your credentials and try again.");
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

//			String url = "http://localhost:4200/reset-password/" + username + "/" + timestamp + "/" + encodedToken;
			String url = "https://jetwayz.vercel.app/reset-password/" + username + "/" + timestamp + "/" + encodedToken;

			System.err.println(url);

//			SimpleMailMessage mailMessage = new SimpleMailMessage();
//			mailMessage.setFrom(sender);
//			mailMessage.setTo(email);
//			mailMessage.setSubject("Password Reset Request");
//			mailMessage.setText("Dear " + user.get().getUsername() + ",\n\n"
//					+ "We received a request to reset your password for the JetWayz. "
//					+ "You can reset your password by clicking the link below:\n\n" + url + "\n\n"
//					+ "If you did not request a password reset, please ignore this email. "
//					+ "For security reasons, this link will expire after a certain period.\n\n" + "Best regards,\n"
//					+ "The Support Team");
//
//			javaMailSender.send(mailMessage);

			try {

				// Create the HTML content for the email
				String htmlContent = "<div style=\"font-family: Arial, sans-serif; padding: 20px; background-color: #f5f5f5;\">"
						+ "<h2 style=\"color: #333;\">JetWayz - Password Reset Request</h2>" + "<p>Dear "
						+ user.get().getName() + ",</p>"
						+ "<p>We received a request to reset your password for JetWayz. "
						+ "You can reset your password by clicking the link below:</p>" + "<p><a href=\"" + url
						+ "\" style=\"background-color: #007bff; color: #fff; padding: 10px 15px; text-decoration: none; border-radius: 5px;\">Reset Password</a></p>"
						+ "<p>If you did not request a password reset, please ignore this email. "
						+ "For security reasons, this link will expire after a certain period.</p>" + "<br>"
						+ "<p>Best regards,<br><strong>The JetWayz Support Team</strong></p>" + "</div>";

				// Create a MimeMessage
				MimeMessage mimeMessage = javaMailSender.createMimeMessage();

				// Create a MimeMessageHelper
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(sender); // Sender's email address
				helper.setTo(email); // Recipient's email address
				helper.setSubject("Password Reset Request");
				helper.setText(htmlContent, true); // Set the HTML content and mark it as HTML

				// Send the email
				javaMailSender.send(mimeMessage);
			}

			catch (Exception e) {
				// TODO: handle exception
				return "Error generated while email sending.";
			}

			return "A password reset link has been sent to your registered email address. "
					+ "Please check your inbox and follow the instructions to reset your password.";
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

		Long time = Long.parseLong(new String(Base64.getDecoder().decode(timestamp)));

		// Now validate the token
		Optional<UserEntity> user = repo.findByUsername(tokenUsername);

		if (user.isPresent()) {
			if (generator.validateToken(time, decodedToken, user.get())) {
				user.get().setUpdatedAt(new Date());
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

	@Override
	public String checkAccountExists(String token) {
		// TODO Auto-generated method stub
		String userName = jwtService.extractUserName(token);
//		System.err.println(userName);

		Optional<UserEntity> user = repo.findByUsername(userName);

		if (user.isEmpty()) {
			return "We couldn't log you in. Your account may have been deleted, or you might not be logged in. Please try again. If the issue persists, consider creating a new account.";
		}
		return "Success.";
	}

	@Override
	public Map<String, Object> getUsersPaginated(int page, int size, String sortField, String sortDirection) {
		try {
			// Create pageable object
			Pageable pageable;
			if (sortField != null && !sortField.isEmpty()) {
				Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending()
						: Sort.by(sortField).descending();
				pageable = PageRequest.of(page, size, sort);
			} else {
				pageable = PageRequest.of(page, size);
			}

			// Get paginated data
			Page<UserEntity> pageResult = repo.findAll(pageable);

			// Map to DTOs
			List<UserProxy> users = MapperUtil.convertListofValue(pageResult.getContent(), UserProxy.class);

			// Create response with pagination metadata
			Map<String, Object> response = new HashMap<>();
			response.put("users", users);
			response.put("currentPage", pageResult.getNumber());
			response.put("totalItems", pageResult.getTotalElements());
			response.put("totalPages", pageResult.getTotalPages());

			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error fetching paginated users: " + e.getMessage(), e);
		}
	}

	@Override
	public long getTotalUsersCount() {
		try {
			return repo.count();
		} catch (Exception e) {
			throw new RuntimeException("Error fetching users count: " + e.getMessage(), e);
		}
	}

	@Override
	public Response downloadAllUserData() {
		try {
            log.info("Entry into downloadAllUserData()");

			List<UserEntity> users = repo.findAll();
			SXSSFWorkbook workbook = new SXSSFWorkbook(100);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			Sheet sheet = workbook.createSheet("Users");
			Row headerRow = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);

            String[] headers = {"Sr. No.", "Username", "Name", "Email", "Mobile", "Role", "Created At", "Updated At"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

			int rowNum = 1;
			for (UserEntity user : users) {
				Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(rowNum);
				row.createCell(1).setCellValue(user.getUsername());
				row.createCell(2).setCellValue(user.getName());
				row.createCell(3).setCellValue(user.getEmailId());
				row.createCell(4).setCellValue(user.getMobileNo());
				row.createCell(5).setCellValue(user.getRole());
				row.createCell(6).setCellValue(user.getCreatedAt().toString());
				row.createCell(7).setCellValue(user.getUpdatedAt().toString());
                rowNum++;
			}

			workbook.write(outputStream);
			workbook.close();

            log.info("Excel file downloaded successfully for all user data !!");

			return Response.builder().data(outputStream.toByteArray()).message("All user data downloaded successfully !")
                    .status_code(HttpStatus.OK.toString()).build();
        } catch (Exception e) {
            log.error("Error generating excel: {}", e.getMessage(), e);
            return Response.builder().message("Error generating excel file")
                    .status_code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).build();
        }
    }
}