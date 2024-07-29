package com.hoangphi.request.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
