package com.hoangphi.controller.user;

import com.hoangphi.request.users.ResetPasswordRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.user.ForgotPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(HttpServletRequest req,
                                                      @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(forgotPasswordService.sendCodeForResetPassword(req, resetPasswordRequest));
    }
    @PostMapping("/verify-forgot")
    public ResponseEntity<ApiResponse> verifyForgot() {
        return ResponseEntity.ok(forgotPasswordService.verifyConfirmResetPasswordEmail());
    }
}
