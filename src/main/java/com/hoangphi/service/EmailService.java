package com.hoangphi.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;


public interface EmailService {
    public void sendVerificationEmail(HttpServletRequest req, String email, UUID otp);
    public void confirmResetPassword(HttpServletRequest req, String email, UUID token);


}
