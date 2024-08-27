package com.hoangphi.service.user;

import com.hoangphi.entity.User;
import com.hoangphi.request.users.CreateUserManageRequest;
import com.hoangphi.request.users.UpdateUserRequest;
import com.hoangphi.response.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    public UserDetails findByUsername(String username);
    public User getUserFromToken(String token);

    public Boolean isAdmin(User user);
    public Boolean isAdmin(String token);

    public ApiResponse updateUser(UpdateUserRequest updateUserRequest);

    public ApiResponse createUser(CreateUserManageRequest createaUserManageRequest);

    public ApiResponse deleteUser(String id);

    public ApiResponse getAllUser(String jwt, Optional<String> keyword,
                                  Optional<String> sort,
                                  Optional<String> role,
                                  Optional<Integer> pages);
    public ApiResponse getUserWithUsername(String username);

    ApiResponse getChart(String userID);
}
