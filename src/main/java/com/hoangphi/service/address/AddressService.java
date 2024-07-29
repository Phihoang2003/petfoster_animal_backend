package com.hoangphi.service.address;

import com.hoangphi.request.address.AddressUserRequest;
import com.hoangphi.response.ApiResponse;

public interface AddressService {
    ApiResponse create(String token, AddressUserRequest data);
    ApiResponse getUserAddresses(String username);
}
