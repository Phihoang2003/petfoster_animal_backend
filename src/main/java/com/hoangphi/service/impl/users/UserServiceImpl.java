package com.hoangphi.service.impl.users;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.Authorities;
import com.hoangphi.entity.Role;
import com.hoangphi.entity.User;
import com.hoangphi.repository.AuthoritiesRepository;
import com.hoangphi.repository.RoleRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.users.CreateUserManageRequest;
import com.hoangphi.request.users.UpdateUserRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.common.PaginationResponse;
import com.hoangphi.response.users.UserProfileResponse;
import com.hoangphi.service.user.UserService;
import com.hoangphi.utils.ImageUtils;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final RoleRepository roleRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PortUtils portUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User exitsUser=userRepository.findByUsername(username).get();
        List<GrantedAuthority> authorities=new ArrayList<>();
        exitsUser.getAuthorities().forEach(authority->
                authorities.add(new SimpleGrantedAuthority(authority.getRole().getRole())));
        return new org.springframework.security.core.userdetails.User(
                exitsUser.getUsername(),
                exitsUser.getPassword(),
                exitsUser.getIsEmailVerified(),
                false,
                false,
                false,
                authorities);
    }

    @Override
    public User getUserFromToken(String token) {
        if(token==null||token.isBlank()){
            return null;
        }
        String userName=securityUtils.getCurrentUsername();
        if(userName==null){
            return null;
        }
        User user=userRepository.findByUsername(userName).orElse(null);
        if(user==null){
            return null;
        }
        return user;
    }

    @Override
    public Boolean isAdmin(User user) {
        boolean isAdmin=false;
        List<Role> manageRoles=roleRepository.managementRoles();
        for(Role role:manageRoles){
            if(role.getRole().equals(user.getAuthorities().get(0).getRole().getRole())){
                isAdmin=true;
            }
        }

        return isAdmin;
    }
    @Override
    public Boolean isAdmin(String token) {

        User user = this.getUserFromToken(token);

        boolean admin = false;
        List<Role> managementRoles = roleRepository.managementRoles();

        for (Role role : managementRoles) {
            if (role.getRole().equals(user.getAuthorities().get(0).getRole().getRole())) {
                admin = true;
            }

        }
        return admin;

    }

    @Override
    public ApiResponse updateUser(UpdateUserRequest updateUserRequest) {
        return null;
    }

    @Override
    public ApiResponse createUser(CreateUserManageRequest createUserManageRequest) {
        Map<String, String> errorsMap = new HashMap<>();
        // start validate

        if (createUserManageRequest.getEmail().isEmpty()) {
            errorsMap.put("email", "Can't update email !");
        }

        if (userRepository.existsByUsername(createUserManageRequest.getUsername())) {
            errorsMap.put("username", "Username " + RespMessage.EXISTS);
        }

        if (userRepository.existsByEmail(createUserManageRequest.getEmail())) {
            errorsMap.put("email", "Email " + RespMessage.EXISTS);
        }

        if (createUserManageRequest.getFullName().isEmpty()) {
            errorsMap.put("fullName", "Fullname can't be blank");
        }

        if (createUserManageRequest.getPhone().isEmpty()) {
            errorsMap.put("phone", "Phone can't be blank");
        }

        if (createUserManageRequest.getBirthday().orElse(null) == null) {
            errorsMap.put("birthday", "Birthday can't be blank");
        }

        if (!errorsMap.isEmpty()) {
            return ApiResponse.builder()
                    .message("Update fail !")
                    .errors(errorsMap)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .build();
        }

        User newUser = User.builder()
                .birthday(createUserManageRequest.getBirthday().orElse(null))
                .email(createUserManageRequest.getEmail())
                .fullname(createUserManageRequest.getFullName())
                .gender(createUserManageRequest.getGender())
                .isActive(true)
                .password(passwordEncoder.encode(createUserManageRequest.getPassword()))
                .phone(createUserManageRequest.getPhone())
                .username(createUserManageRequest.getUsername())
                .isEmailVerified(true)
                .build();

        // set role and authorization
        List<Authorities> authoritiesList = new ArrayList<>();
        Authorities authorities = Authorities.builder().user(newUser).role(roleRepository.getRoleUser()).build();
        authoritiesList.add(authorities);

        newUser.setAuthorities(authoritiesList);

        if (createUserManageRequest.getAvatar() != null) {
            if (createUserManageRequest.getAvatar().getSize() > 500000) {
                errorsMap.put("avatar", "Image size is too large");
            } else {
                try {
                    File file = ImageUtils.createFileImage();

                    createUserManageRequest.getAvatar().transferTo(new File(file.getAbsolutePath()));
                    newUser.setAvatar(file.getName());
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

        return ApiResponse.builder()
                .message("Create successfully !")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(userRepository.save(newUser))
                .build();
    }

    @Override
    public ApiResponse deleteUser(String id) {
        return null;
    }

    @Override
    public ApiResponse getAllUser(String jwt, Optional<String> keyword, Optional<String> sort, Optional<String> role, Optional<Integer> pages) {
        List<User> users = userRepository.findAll(keyword.orElse(null), role.orElse(null), sort.orElse(null));

        if (users.isEmpty()) {
            return ApiResponse.builder().message("No data!")
                    .status(400)
                    .errors(false)
                    .data(PaginationResponse.builder().data(new ArrayList<>())
                            .build())
                    .build();
        }

        Pageable pageable = PageRequest.of(pages.orElse(0), 10);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), users.size());

        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        List<User> visibleUsers = users.subList(startIndex, endIndex);

        List<UserProfileResponse> visibleResponseUsers = new ArrayList<>();

        visibleUsers.forEach((user) -> {
            // find role by user
            List<Authorities> roles = authoritiesRepository.findByUser(user);
            Role userRole = null;
            if (!roles.isEmpty()) {
                userRole = roles.get(0).getRole();
            }
            UserProfileResponse userProfile = UserProfileResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .fullName(user.getFullname())
                    .birthday(user.getBirthday())
                    .gender(user.getGender() != null)
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .displayName(user.getDisplayName())
                    .provider(user.getProvider())
                    .avatar(user.getAvatar() == null ? null : portUtils.getUrlImage(user.getAvatar()))
                    .role(userRole == null ? null : userRole.getRole())
                    .createAt(user.getCreatedAt())
                    .build();

            visibleResponseUsers.add(userProfile);

        });

        Page<UserProfileResponse> pagination = new PageImpl<UserProfileResponse>(visibleResponseUsers, pageable,
                users.size());

        return ApiResponse.builder().message("Successfully!")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(PaginationResponse.builder()
                        .data(pagination.getContent())
                        .pages(pagination.getTotalPages())
                        .build())
                .build();
    }

    @Override
    public ApiResponse getUserWithUsername(String username) {
        return null;
    }

    @Override
    public ApiResponse getChart(String userID) {
        return null;
    }

    public UserProfileResponse buildUserProfileResponse(User user){
        List<Authorities> roles=authoritiesRepository.findByUser(user);
        Role role=null;
        if(!roles.isEmpty()){
            role=roles.get(0).getRole();
        }

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullname())
                .birthday(user.getBirthday())
                .gender(user.getGender() != null)
                .phone(user.getPhone())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .provider(user.getProvider())
                .avatar(user.getAvatar()==null?null:portUtils.getUrlImage(user.getAvatar()))
                .role(role == null ? null : role.getRole())
                .createAt(user.getCreatedAt())
                .build();
    }
}
