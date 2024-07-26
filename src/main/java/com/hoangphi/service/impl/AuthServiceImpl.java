package com.hoangphi.service.impl;

import com.hoangphi.constant.PatternExpression;
import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.User;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.RegisterRequest;
import com.hoangphi.response.AuthResponse;
import com.hoangphi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

     UserRepository userRepository;
     PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(HttpServletRequest httpServletRequest, RegisterRequest registerReq) {
        Map<String,String> errorsMap=new HashMap<>();
        if(PatternExpression.NOT_SPECIAL.matcher(registerReq.getUsername()).find()){
            errorsMap.put("username","Username must not contain special characters");
            return AuthResponse.builder()
                    .message(HttpStatus.BAD_REQUEST.toString())
                    .errors(errorsMap)
                    .build();

        }
        if(userRepository.existsByUsername(registerReq.getUsername())){
             errorsMap.put("username","Username"+ RespMessage.EXISTS);
            return AuthResponse.builder()
                    .message(HttpStatus.CONFLICT.toString())
                    .errors(errorsMap)
                    .build();
        }
        if(userRepository.existsByEmail(registerReq.getEmail())){
            errorsMap.put("email","Email"+ RespMessage.EXISTS);
            return AuthResponse.builder()
                    .message(HttpStatus.CONFLICT.toString())
                    .errors(errorsMap)
                    .build();
        }
        if(!registerReq.getPassword().equals(registerReq.getConfirmPassword())){
            errorsMap.put("password","Password and Confirm Password must be the same");
            return AuthResponse.builder()
                    .message(HttpStatus.CONFLICT.toString())
                    .errors(errorsMap)
                    .build();
        }
        User newUser=User.builder()
                .username(registerReq.getUsername())
                .email(registerReq.getEmail())
                .displayName(registerReq.getUsername())
                .password(passwordEncoder.encode(registerReq.getPassword()))
                .gender(registerReq.getGender())
                .fullname(registerReq.getFullname())
                .createdAt(new Date())
                .isActive(true)
                .provider("local")
                .isEmailVerified(false)
                .build();

        String token=sendToken(httpServletRequest,registerReq.getEmail());









        return null;

    }

    public UUID sendToken(HttpServletRequest httpServletRequest, String email) {
        UUID token=UUID.randomUUID();

        return null;
    }


}
