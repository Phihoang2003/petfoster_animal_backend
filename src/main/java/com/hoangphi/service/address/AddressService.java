package com.hoangphi.service.address;

import com.hoangphi.request.address.AddressUserRequest;
import com.hoangphi.response.ApiResponse;

public interface AddressService {
    ApiResponse create(String token, AddressUserRequest data);
    ApiResponse getUserAddresses(String username);

    ApiResponse getAddressesByToken(String token);

    ApiResponse getAddressById(String token, Integer id);

    ApiResponse findDefaultAddress(String token);
    ApiResponse update(String token, Integer id, AddressUserRequest data);
}
