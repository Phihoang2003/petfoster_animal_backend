package com.hoangphi.request;

import com.hoangphi.constant.RespMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

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
