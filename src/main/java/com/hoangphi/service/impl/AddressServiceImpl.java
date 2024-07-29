package com.hoangphi.service.impl;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.Addresses;
import com.hoangphi.entity.User;
import com.hoangphi.repository.AddressRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.address.AddressRequest;
import com.hoangphi.request.address.AddressUserRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public String getUsernameFromToken(String token){
        return jwtProvider.getUsernameFromToken(token);
    }

    @Override
    public ApiResponse create(String token, AddressUserRequest data) {
        String username=getUsernameFromToken(token);
        System.out.println("username: "+username);
        if(username==null||username.isEmpty()){
            return ApiResponse.builder()
                    .status(400)
                    .message("Please login to use !")
                    .errors(true)
                    .data(null)
                    .build();
        }
        Optional<User> user=userRepository.findByUsername(username);
        if(user.isEmpty()){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(RespMessage.NOT_FOUND.getValue())
                    .errors(true)
                    .data(null)
                    .build();
        }
        int condition=4;
        if(addressRepository.findByUser(user.get()).size()>=condition){
            return ApiResponse.builder()
                    .status(400)
                    .message("Can't add new address, becase The number of addresses exceeds the specified limit ( " + 4
                            + " ) ")
                    .errors(true)
                    .data(null)
                    .build();
        }
        Addresses addresses=buildAddresses(data,user.get());
        if(addresses==null){
            return ApiResponse.builder()
                    .message(RespMessage.INVALID.getValue())
                    .status(405)
                    .errors(true)
                    .data(null)
                    .build();
        }
        return ApiResponse.builder()
                .message(RespMessage.SUCCESS.getValue())
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(addressRepository.save(addresses))
                .build();

    }
    private Addresses buildAddresses(AddressUserRequest addressUserRequest, User user){
        if(addressUserRequest==null){
            return null;
        }
        boolean isDefault=addressUserRequest.isSetDefault();
        List<Addresses> list=addressRepository.findByUser(user);
        if(list==null||list.size()<=0){
            isDefault=true;
        }
        Addresses defaultAddress=addressRepository.findByIsDefaultWithUser(user.getUsername());
        if(defaultAddress!=null&&isDefault){
            defaultAddress.setIsDefault(false);
            addressRepository.save(defaultAddress);
        }
        return Addresses.builder()
                .isDefault(isDefault)
                .address(addressUserRequest.getAddress().getAddress())
                .province(addressUserRequest.getAddress().getProvince())
                .district(addressUserRequest.getAddress().getDistrict())
                .phone(addressUserRequest.getPhone())
                .ward(addressUserRequest.getAddress().getWard())
                .recipient(addressUserRequest.getName())
                .user(user)
                .build();

    }
}
