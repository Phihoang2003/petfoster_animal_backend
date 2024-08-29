package com.hoangphi.response.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotBlank
    @Length(min = 6, message = "must be longer than 6 characters!")
    private String password;
    @NotBlank
    @Length(min = 6, message = "must be longer than 6 characters!")
    private String newPassword;
}
