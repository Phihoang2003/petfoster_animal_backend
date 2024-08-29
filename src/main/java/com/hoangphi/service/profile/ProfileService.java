package com.hoangphi.service.profile;

import com.hoangphi.request.profile.UserProfileRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.users.ChangePasswordRequest;

public interface ProfileService {
    ApiResponse getProfile();

    ApiResponse updateProfile(UserProfileRequest profileRequest);

    ApiResponse changePassword(ChangePasswordRequest changePasswordRequest);
}
