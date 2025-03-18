package com.flight.management.service.impl;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.flight.management.domain.UserEntity;

//@Component
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final UserEntity user;

    // This constructor gets the UserEntity object
    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Other required methods can be implemented
//    @Override
//    public boolean isAccountNonExpired() {
//        return true; // You can adjust this based on your requirements
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true; // Adjust if necessary
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true; // Adjust if necessary
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true; // Adjust if necessary
//    }
}
