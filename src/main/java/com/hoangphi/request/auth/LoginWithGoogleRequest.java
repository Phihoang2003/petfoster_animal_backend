package com.hoangphi.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginWithGoogleRequest {
    private String uuid;
    private String username;
    private String avartar;
    private String email;
}
