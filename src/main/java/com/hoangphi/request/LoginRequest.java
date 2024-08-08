package com.hoangphi.request;

import com.hoangphi.constant.RespMessage;
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
public class LoginRequest {
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String username;

    @NotBlank(message = RespMessage.NOT_EMPTY)
    @Length(min = 6, message = "must be longer than 6 characters!")
    private String password;
}
