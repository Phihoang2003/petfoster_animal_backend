package com.hoangphi.response.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String id;
    private String username;
    private String fullname;
    private Date birthday;
    private boolean gender;
    private String phone;
    private String email;
    private String avatar;
    private String role;
    private String displayName;
    private String provider;
    private Date createAt;

}
