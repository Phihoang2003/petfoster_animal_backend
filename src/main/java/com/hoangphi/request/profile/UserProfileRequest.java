package com.hoangphi.request.profile;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileRequest {
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String fullName;

    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String phone;
    @NotEmpty(message = RespMessage.NOT_EMPTY)
    @Email(message = "is invalid")
    private String email;

    @NotNull(message = RespMessage.NOT_EMPTY)
    private Boolean gender;

    @NotBlank(message = RespMessage.NOT_EMPTY)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Optional<LocalDate> birthday;

    private MultipartFile avatar;
}
