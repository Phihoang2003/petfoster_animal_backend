package com.hoangphi.response.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressUserResponse {
    private Integer id;
    private String name;
    private String phone;
    private AddressResponse address;
    private boolean isDefault;

}
