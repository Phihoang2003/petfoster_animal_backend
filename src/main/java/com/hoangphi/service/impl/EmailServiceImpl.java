package com.hoangphi.service.impl;

import com.hoangphi.service.EmailService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class EmailServiceImpl implements EmailService {
    @Override
    public void sendVerificationEmail(HttpServletRequest req, String email, UUID otp) {

    }

    @Override
    public void confirmResetPassword(HttpServletRequest req, String email, UUID token) {

    }
}
