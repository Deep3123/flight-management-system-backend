package com.flight.management.filter;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.flight.management.service.impl.CustomUserDetailsService;
import com.flight.management.util.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private CustomUserDetailsService service;

	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// Log all header names for debugging
//		Enumeration<String> headerNames = request.getHeaderNames();
//		while (headerNames.hasMoreElements()) {
//			String headerName = headerNames.nextElement();
//			System.out.println("Header: " + headerName + " = " + request.getHeader(headerName));
//		}

		String path = request.getRequestURI();
		if (path.equals("/oauth/complete-profile")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = jwtService.getTokenFromRequest(request);
//		System.err.println(token);

		if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			token = token.substring(7);
			String username = jwtService.extractUserName(token);

			UserDetails user = service.loadUserByUsername(username);

			if (user != null && jwtService.validateToken(token, user)) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		filterChain.doFilter(request, response);

	}
}
