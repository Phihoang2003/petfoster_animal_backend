package com.hoangphi.request;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


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
