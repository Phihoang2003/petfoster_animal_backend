package com.hoangphi.service.user;

import com.hoangphi.request.users.ResetPasswordRequest;
import com.hoangphi.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;


public interface ForgotPasswordService {
    public ApiResponse sendCodeForResetPassword(HttpServletRequest req, ResetPasswordRequest resetPasswordRequest);

    public ApiResponse verifyConfirmResetPasswordEmail();
}
