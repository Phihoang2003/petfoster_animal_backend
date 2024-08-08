package com.hoangphi.request.address;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressUserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    private AddressRequest address;
    private boolean setDefault;
}
