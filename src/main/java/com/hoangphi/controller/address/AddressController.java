package com.hoangphi.controller.address;

import com.hoangphi.request.address.AddressRequest;
import com.hoangphi.request.address.AddressUserRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> create(@RequestHeader("Authorization") String token, @Valid @RequestBody AddressUserRequest data){
        return ResponseEntity.ok(addressService.create(token, data));
    }
}
