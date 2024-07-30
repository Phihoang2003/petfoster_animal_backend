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
//    @GetMapping("/{username}")
//    public ResponseEntity<ApiResponse> getUserAddresses(@PathVariable("username") String username){
//        return ResponseEntity.ok(addressService.getUserAddresses(username));
//    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAddressesByToken(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(addressService.getAddressesByToken(token));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAddressById(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id){
        return ResponseEntity.ok(addressService.getAddressById(token, id));
    }
    @GetMapping("/default")
    public ResponseEntity<ApiResponse> findDefaultAddress(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(addressService.findDefaultAddress(token));
    }
}
