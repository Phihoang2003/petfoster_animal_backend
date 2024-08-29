package com.hoangphi.response.profile;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String id;

    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String username;

    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String fullName;

    @NotEmpty(message = RespMessage.NOT_EMPTY)
    @Email(message = "is invalid")
    private String email;

    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String phone;

    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Boolean gender;

    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Date birthday;
}
