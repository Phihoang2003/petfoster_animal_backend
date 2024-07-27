package com.hoangphi.service.impl;

import com.hoangphi.entity.User;
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
}
