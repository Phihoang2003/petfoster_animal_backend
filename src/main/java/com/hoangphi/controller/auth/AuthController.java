package com.hoangphi.controller.auth;

import com.hoangphi.request.LoginRequest;
import com.hoangphi.request.RegisterRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.AuthResponse;
import com.hoangphi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
public class AuthController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
     AuthService authService;
    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(HttpServletRequest httpServletRequest, @Valid @RequestBody RegisterRequest registerRequest){
        httpServletRequest.setAttribute("username", registerRequest.getUsername());
        return ResponseEntity.ok(authService.register(httpServletRequest, registerRequest));
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verify(@RequestParam("code") String code){
        return ResponseEntity.ok(authService.verifyEmail(code));
    }
}
