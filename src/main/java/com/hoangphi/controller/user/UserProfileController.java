package com.hoangphi.controller.user;

import com.hoangphi.request.profile.UserProfileRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserProfileController {
    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile() {

        return ResponseEntity.ok(profileService.getProfile());
    }

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(@ModelAttribute("user") UserProfileRequest profileRequest) {
        return ResponseEntity.ok(profileService.updateProfile(profileRequest));
    }
}
