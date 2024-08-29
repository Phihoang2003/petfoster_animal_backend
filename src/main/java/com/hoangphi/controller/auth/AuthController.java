package com.hoangphi.controller.auth;

import com.hoangphi.request.LoginRequest;
import com.hoangphi.request.RegisterRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.AuthResponse;
import com.hoangphi.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final HttpServletRequest httpServletRequest;
    @Qualifier("authServiceImpl")
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(HttpServletRequest httpServletRequest, @Valid @RequestBody RegisterRequest registerRequest){
        httpServletRequest.setAttribute("username", registerRequest.getUsername());
        return ResponseEntity.ok(authService.register(httpServletRequest, registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verify(@RequestParam("code") String code){
        return ResponseEntity.ok(authService.verifyEmail(code));
    }
}
