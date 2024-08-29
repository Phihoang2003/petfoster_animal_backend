package com.hoangphi.service.impl.profile;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.entity.Authorities;
import com.hoangphi.entity.Role;
import com.hoangphi.entity.User;
import com.hoangphi.repository.AuthoritiesRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.profile.UserProfileRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.users.ChangePasswordRequest;
import com.hoangphi.response.users.UserProfileResponse;
import com.hoangphi.service.profile.ProfileService;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecurityUtils  securityUtils;
    private final AuthoritiesRepository authoritiesRepository;
    private final PortUtils portUtils;

    @Override
    public ApiResponse getProfile() {
        String username = securityUtils.getCurrentUsername();

        User user = userRepository.findByUsername(username).orElse(null);

        // find role by user
        Role role = authoritiesRepository.findByUser(user).get(0).getRole();

        // check if user no role
        if (role == null) {
            return ApiResponse.builder()
                    .message("Get failure!!")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errors(true)
                    .data(null)
                    .build();
        }

        // build a user profile
        assert user != null;
        UserProfileResponse userProfile = UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullname())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar() == null ? null : portUtils.getUrlImage(user.getAvatar()))
                .role(role.getRole())
                .displayName(user.getDisplayName())
                .provider(user.getProvider())
                .createAt(user.getCreatedAt())
                .build();

        return ApiResponse.builder()
                .message("Successfully!")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(userProfile)
                .build();
    }

    @Override
    public ApiResponse updateProfile(UserProfileRequest profileRepuest, String token) {
        return null;
    }

    @Override
    public ApiResponse changePassword(ChangePasswordRequest changePasswordRequest, String token) {
        return null;
    }
}
