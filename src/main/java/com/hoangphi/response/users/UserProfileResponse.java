package com.hoangphi.response.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String id;
    private String username;
    private String fullName;
    private LocalDate birthday;
    private boolean gender;
    private String phone;
    private String email;
    private String avatar;
    private String role;
    private String displayName;
    private String provider;
    private LocalDate createAt;

}
