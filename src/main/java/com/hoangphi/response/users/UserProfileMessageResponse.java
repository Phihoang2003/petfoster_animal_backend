package com.hoangphi.response.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProfileMessageResponse {
    private String id;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String avatar;
}
