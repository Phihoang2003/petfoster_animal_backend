package com.hoangphi.service.profile;

import com.hoangphi.request.profile.UserProfileRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.users.ChangePasswordRequest;

public interface ProfileService {
    ApiResponse getProfile();

    ApiResponse updateProfile(UserProfileRequest profileRepuest, String token);

    ApiResponse changePassword(ChangePasswordRequest changePasswordRequest, String token);
}
