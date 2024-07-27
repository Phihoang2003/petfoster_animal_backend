package com.hoangphi.request;

import com.hoangphi.constant.RespMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String username;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    @Length(min = 6, message = "must be longer than 6 characters!")
    private String password;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    @Length(min = 6, message = "must be longer than 6 characters!")
    private String confirmPassword;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String fullname;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    @Email(message = "is invalid")
    private String email;
    @NotNull(message = RespMessage.NOT_EMPTY)
    private Boolean gender;
}
