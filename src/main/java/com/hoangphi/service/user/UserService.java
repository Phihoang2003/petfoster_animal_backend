package com.hoangphi.service.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    public UserDetails findByUsername(String username);
}
