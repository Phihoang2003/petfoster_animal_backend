package com.hoangphi.service.impl.users;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.entity.User;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.users.ResetPasswordRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.impl.EmailServiceImpl;
import com.hoangphi.service.user.ForgotPasswordService;
import com.hoangphi.utils.MailUtils;
import com.hoangphi.utils.RandomPassword;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final UserRepository userRepository;
    private final EmailServiceImpl emailServiceImpl;
    private final MailUtils mailUtils;
    private final PasswordEncoder passwordEncoder;
    private final RandomPassword randomPassword;
    private final SecurityUtils securityUtils;

    @Override
    public ApiResponse sendCodeForResetPassword(HttpServletRequest req, ResetPasswordRequest resetPasswordRequest) {
        User existsUser = userRepository.findByEmail(resetPasswordRequest.getEmail()).orElse(null);
        if (existsUser == null) {
            return ApiResponse.builder().data("").message("user is not exist").status(404)
                    .errors(false).build();
        }

        // check active user
        if (!existsUser.getIsActive()) {
            return ApiResponse.builder().data("").message("user is not active").status(404)
                    .errors(false).build();
        }

        // check email verification
        if (!existsUser.getIsEmailVerified()) {
            return ApiResponse.builder().data("").message("email has not been verified").status(404)
                    .errors(false).build();
        }

        String email = existsUser.getEmail();
        String token = sendToken(req, email).toString();
        existsUser.setToken(token);
        userRepository.save(existsUser);
        return ApiResponse.builder()
                .data("The reset password code has been sent to your email !")
                .message("Successfully!")
                .status(HttpStatus.OK.value())
                .errors(true).build();
    }

    @Override
    public ApiResponse verifyConfirmResetPasswordEmail() {
        User existsUser = userRepository.findByUsername(securityUtils.getCurrentUsername()).orElse(null);

        if (existsUser == null) {
                return ApiResponse.builder()
                        .message("Token is not exists")
                        .status(HttpStatus.NOT_FOUND.value())
                        .errors(true)
                        .build();
        }
        String newPassword = randomPassword.randomPassword();
        String newPasswordEncode = passwordEncoder.encode(newPassword);
        existsUser.setPassword(newPasswordEncode);

        userRepository.save(existsUser);

        String toEmail = existsUser.getEmail();
        String subject = "Reset password!";
        String body = "Hello " + existsUser.getFullname() +
                "\n You are performing a password update, your new password is " + newPassword;
        mailUtils.sendEmail(toEmail, subject, body);
        return ApiResponse.builder()
                .data(existsUser)
                .message("Your password has been reset! Please check email!")
                .status(HttpStatus.OK.value())
                .errors(false)
                .build();
    }
    public UUID sendToken(HttpServletRequest req, String email) {
        UUID token = UUID.randomUUID();
        emailServiceImpl.confirmResetPassword(req, email, token);
        return token;
    }
}
