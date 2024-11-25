package com.hoangphi.service.auth;

import com.hoangphi.request.LoginRequest;
import com.hoangphi.request.RegisterRequest;
import com.hoangphi.request.auth.LoginWithGoogleRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;


public interface AuthService {
    Authentication authenticate(String username, String password);
    ApiResponse verifyEmail(String token);
    AuthResponse login(LoginRequest loginReq);
    AuthResponse register(HttpServletRequest httpServletRequest, RegisterRequest registerReq);
    AuthResponse loginWithGoogle(LoginWithGoogleRequest loginWithGoogleResquest);
}
