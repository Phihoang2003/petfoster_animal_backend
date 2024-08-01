package com.hoangphi.service.impl.users;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.entity.Role;
import com.hoangphi.entity.User;
import com.hoangphi.repository.RoleRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.service.user.UserService;
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
}
