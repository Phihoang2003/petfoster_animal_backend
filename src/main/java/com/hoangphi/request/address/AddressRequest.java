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
public class AddressRequest {
    @NotBlank
    private String province;
    @NotBlank
    private String district;
    @NotBlank
    private String ward;
    @NotBlank
    private String address;
}
