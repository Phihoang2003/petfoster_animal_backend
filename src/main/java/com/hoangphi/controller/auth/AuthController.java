package com.hoangphi.controller.auth;

import com.hoangphi.request.RegisterRequest;
import com.hoangphi.response.AuthResponse;
import com.hoangphi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(HttpServletRequest httpServletRequest, @Valid @RequestBody RegisterRequest registerRequest){
        httpServletRequest.setAttribute("username", registerRequest.getUsername());
        return ResponseEntity.ok(authService.register(httpServletRequest, registerRequest));
    }
}
