package com.hoangphi.service.impl.users;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.entity.Authorities;
import com.hoangphi.entity.Role;
import com.hoangphi.entity.User;
import com.hoangphi.repository.AuthoritiesRepository;
import com.hoangphi.repository.RoleRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.response.users.UserProfileResponse;
import com.hoangphi.service.user.UserService;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PortUtils portUtils;
    @Override
    public UserDetails findByUsername(String username) {
        User exitsUser=userRepository.findByUsername(username).get();
        List<GrantedAuthority> authorities=new ArrayList<>();
        exitsUser.getAuthorities().forEach(authority->authorities.add(new SimpleGrantedAuthority(authority.getRole().getRole())));
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
        String userName=jwtProvider.getUsernameFromToken(token);
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
    public UserProfileResponse buildUserProfileResponse(User user){
        List<Authorities> roles=authoritiesRepository.findByUser(user);
        Role role=null;
        if(!roles.isEmpty()){
            role=roles.get(0).getRole();
        }

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullname())
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
