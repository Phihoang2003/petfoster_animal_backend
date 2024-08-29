package com.hoangphi.request.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserManageRequest {
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private Boolean gender;
    private String role;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Optional<LocalDate> birthday;
    private String address;
    private MultipartFile avatar;
}
