package com.flight.management.service;

import com.flight.management.proxy.LoginResp;
import com.flight.management.proxy.OAuthProfileCompletion;

public interface OAuthService {
    LoginResp completeProfile(OAuthProfileCompletion profileData, String token) throws Exception;
}
