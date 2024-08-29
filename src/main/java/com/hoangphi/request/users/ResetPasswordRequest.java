package com.hoangphi.request.users;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty(message = RespMessage.NOT_EMPTY)
    @Email(message = "is invalid")
    private String email;
}
