package com.hoangphi.service.impl;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.constant.Constant;
import com.hoangphi.constant.PatternExpression;
import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.Authorities;
import com.hoangphi.entity.User;
import com.hoangphi.repository.RoleRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.LoginRequest;
import com.hoangphi.request.RegisterRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.AuthResponse;
import com.hoangphi.service.AuthService;
import com.hoangphi.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service("authServiceImpl")
@RequiredArgsConstructor
//@Component("userDetailsService")
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailServiceImpl emailServiceimpl;

    private final RoleRepository  roleRepository;
    private final SecurityUtils securityUtils;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public AuthResponse login(LoginRequest loginReq) {
        String username=loginReq.getUsername();
        String password=loginReq.getPassword();
        Map<String,String> errorsMap=new HashMap<>();
        UserDetails userDetails;
        try{
            userDetails=userService.loadUserByUsername(loginReq.getUsername());

        }catch(Exception e){
            errorsMap.put("username","Username not found");
            return AuthResponse.builder()
                    .message(HttpStatus.NOT_FOUND.toString())
                    .errors(errorsMap)
                    .build();
        }
        if(!passwordEncoder.matches(loginReq.getPassword(),userDetails.getPassword())){
            errorsMap.put("username", "Username is incorrect");
            errorsMap.put("password","Password is incorrect, please try again!");
            return AuthResponse.builder()
                    .message(HttpStatus.BAD_REQUEST.toString())
                    .errors(errorsMap)
                    .build();
        }
        if(!userDetails.isEnabled()){
            errorsMap.put("Email: ","your email is not verified, please check your email to verify");
            return AuthResponse.builder()
                    .message(HttpStatus.UNAUTHORIZED.value()+"")
                    .errors(errorsMap)
                    .build();
        }
        Authentication authentication=authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=securityUtils.createToken(authentication);
        return AuthResponse.builder()
                .message("Login successfully")
                .token(token)
                .errors(false)
                .build();

    }
    @Override
    public Authentication authenticate(String username, String password) {
        UserDetails userDetails=userService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    @Override
    public ApiResponse verifyEmail(String token) {
        User user=userRepository.findByToken(token);
        if(user==null){
            return ApiResponse.builder()
                    .message("Token not found")
                    .status(404)
                    .errors(true)
                    .build();
        }
        if(user.getIsEmailVerified()){
            return ApiResponse.builder()
                    .message("Email has been verified")
                    .status(400)
                    .errors(true)
                    .build();
        }
        if (Duration.between(user.getTokenCreatedAt(), LocalDateTime.now()).toMillis() > Constant.TOKEN_EXPIRE_LIMIT) {
            return ApiResponse.builder()
                    .message("Token is expired")
                    .status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value())
                    .errors(true)
                    .build();
        }
        user.setIsEmailVerified(true);
        userRepository.save(user);
        return ApiResponse.builder()
                .message("Email has been verified")
                .status(200)
                .errors(false)
                .data(user)
                .build();

    }

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
             errorsMap.put("username","Username "+ RespMessage.EXISTS);
            return AuthResponse.builder()
                    .message(HttpStatus.CONFLICT.toString())
                    .errors(errorsMap)
                    .build();
        }
        if(userRepository.existsByEmail(registerReq.getEmail())){
            errorsMap.put("email","Email "+ RespMessage.EXISTS);
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
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .provider("local")
                .isEmailVerified(false)
                .build();

        String token=sendToken(httpServletRequest,registerReq.getEmail()).toString();
        newUser.setToken(token);
        userRepository.save(newUser);
        List<Authorities> authoritiesList=new ArrayList<>();
        Authorities authorities=Authorities.builder()
                .user(newUser)
                .role(roleRepository.getRoleUser())
                .build();
        authoritiesList.add(authorities);
        newUser.setAuthorities(authoritiesList);
        userRepository.save(newUser);

        return AuthResponse.builder()
                .message("Register successfully")
                .errors(false)
                .build();

    }


    public UUID sendToken(HttpServletRequest httpServletRequest, String email) {
        UUID token=UUID.randomUUID();
        CompletableFuture.runAsync(()->emailServiceimpl.sendVerificationEmail(httpServletRequest,email,token));
        return token;
    }

}
