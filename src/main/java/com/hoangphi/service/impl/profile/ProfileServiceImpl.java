package com.hoangphi.service.impl.profile;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.constant.RespMessage;
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
import com.hoangphi.utils.ImageUtils;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    public ApiResponse updateProfile(UserProfileRequest profileRequest) {
        Map<String, String> errorsMap = new HashMap<>();
        String username = securityUtils.getCurrentUsername();

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ApiResponse.builder()
                    .message("User not found !")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }

        if (user.getProvider() == null || !user.getProvider().equals("facebook")) {
            if (!user.getEmail().equals(profileRequest.getEmail()) && user.getUuid() == null) {
                errorsMap.put("email", "Can't update email !");
            }
        }

        if (profileRequest.getFullName().isEmpty()) {
            errorsMap.put("fullName", "FullName can't be blank");
        }

        if (profileRequest.getEmail().isEmpty()) {
            errorsMap.put("email", "Email can't be blank");
        }

        if (profileRequest.getPhone().isEmpty()) {
            errorsMap.put("phone", "Phone can't be blank");
        }

        if (profileRequest.getBirthday().orElse(null) == null) {
            errorsMap.put("birthday", "Birthday can't be blank");
        } else {
            user.setBirthday(profileRequest.getBirthday().orElse(null));
        }

        if (!errorsMap.isEmpty()) {
            return ApiResponse.builder()
                    .message("Update failed !")
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }

        if (profileRequest.getAvatar() != null) {
            if (profileRequest.getAvatar().getSize() > 500000) {
                errorsMap.put("avatar", "Image size is too large");
            } else {
                try {
                    File file = ImageUtils.createFileImage();

                    profileRequest.getAvatar().transferTo(new File(file.getAbsolutePath()));
                    user.setAvatar(file.getName());
                } catch (Exception e) {
                    System.out.println("Error in update avatar in Profile service impl");
                    e.printStackTrace();
                    return ApiResponse.builder()
                            .message(RespMessage.INTERNAL_SERVER_ERROR.getValue())
                            .errors(true)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .data(null)
                            .build();
                }
            }
        }

        if (!errorsMap.isEmpty()) {
            return ApiResponse.builder()
                    .message("Update failed !")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }

        user.setFullname(profileRequest.getFullName());
        user.setGender(profileRequest.getGender());
        user.setPhone(profileRequest.getPhone());

        if (user.getProvider() != null) {
            if (user.getUuid() != null && user.getProvider().equals("facebook") &&
                    user.getEmail() == null) {
                user.setEmail(profileRequest.getEmail());
            }
        }

        User newUser = userRepository.save(user);

        newUser.setAvatar(portUtils.getUrlImage(user.getAvatar()));

        return ApiResponse.builder()
                .message("Update success!")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(newUser)
                .build();
    }

    @Override
    public ApiResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        Map<String, String> errorsMap = new HashMap<>();

        String username = securityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ApiResponse.builder()
                    .message("User not found !")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }

        if (!passwordEncoder.matches(changePasswordRequest.getPassword(), user.getPassword())) {
            errorsMap.put("password", "password is incorrect, please try again!!");
            return ApiResponse.builder()
                    .message(RespMessage.FAILURE.getValue())
                    .errors(errorsMap)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .build();
        }

        // all good
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        return ApiResponse.builder()
                .message("Update success!")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(userRepository.save(user))
                .build();
    }

}
