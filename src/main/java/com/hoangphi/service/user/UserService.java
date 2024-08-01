package com.hoangphi.service.user;

import com.hoangphi.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    public UserDetails findByUsername(String username);
    public User getUserFromToken(String token);

    public Boolean isAdmin(User user);
}
