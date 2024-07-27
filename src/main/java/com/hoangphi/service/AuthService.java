package com.hoangphi.service;

import com.hoangphi.request.RegisterRequest;
import com.hoangphi.response.AuthResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthResponse register(HttpServletRequest httpServletRequest, RegisterRequest registerReq);
}
