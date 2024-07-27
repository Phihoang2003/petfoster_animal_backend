package com.hoangphi.service;

import com.hoangphi.request.LoginRequest;
import com.hoangphi.request.RegisterRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.AuthResponse;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    Authentication authenticate(String username, String password);
    ApiResponse verifyEmail(String token);
    AuthResponse login(LoginRequest loginReq);
    AuthResponse register(HttpServletRequest httpServletRequest, RegisterRequest registerReq);
}
