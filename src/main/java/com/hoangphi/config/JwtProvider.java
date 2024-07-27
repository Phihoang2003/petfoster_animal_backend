package com.hoangphi.config;

import com.hoangphi.constant.Constant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtProvider {
    SecretKey key= Keys.hmacShaKeyFor(Constant.SECRET_KEY.getBytes());
    public String generateToken(Authentication authentication){
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+3600000))
                .claim("username",authentication.getName())
                .claim("authorities",authentication.getAuthorities())
                .signWith(key).compact();
    }


}
