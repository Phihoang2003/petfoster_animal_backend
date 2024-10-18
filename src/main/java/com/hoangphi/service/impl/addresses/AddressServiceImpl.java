package com.hoangphi.service.impl.addresses;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.Addresses;
import com.hoangphi.entity.User;
import com.hoangphi.repository.AddressRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.address.AddressUserRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.address.AddressResponse;
import com.hoangphi.response.address.AddressUserResponse;
import com.hoangphi.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public String getUsernameFromToken(){
        return securityUtils.getCurrentUsername();
    }

    @Override
    public ApiResponse create(String token, AddressUserRequest data) {
        String username=getUsernameFromToken();
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
    private AddressUserResponse buildAddressResponse(Addresses addresses) {

        if (addresses == null)
            return null;

        return AddressUserResponse.builder()
                .id(addresses.getId())
                .name(addresses.getRecipient())
                .phone(addresses.getPhone())
                .address(
                        AddressResponse.builder()
                                .province(addresses.getProvince())
                                .district(addresses.getDistrict())
                                .ward(addresses.getWard())
                                .address(addresses.getAddress())
                                .build())
                .isDefault(addresses.getIsDefault())
                .build();
    }

    @Override
    public ApiResponse getUserAddresses(String username) {
        User user=userRepository.findByUsername(username).orElse(null);
        if(user==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(RespMessage.NOT_FOUND.getValue())
                    .errors(true)
                    .data(new ArrayList<>())
                    .build();
        }
        List<Addresses> list=addressRepository.findByUser(user);
        List<AddressUserResponse> lists=new ArrayList<>();
        for(Addresses a:list){
            lists.add(buildAddressResponse(a));
        }
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(RespMessage.SUCCESS.getValue())
                .errors(false)
                .data(lists)
                .build();
    }

    @Override
    public ApiResponse getAddressesByToken(String token) {
        String username=getUsernameFromToken();
        if(username==null||username.isEmpty()){
            return ApiResponse.builder()
                    .status(400)
                    .message("Please login to use !")
                    .errors(true)
                    .data(null)
                    .build();
        }
        User user=userRepository.findByUsername(username).orElse(null);
        if(user==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(RespMessage.NOT_FOUND.getValue())
                    .errors(true)
                    .data(new ArrayList<>())
                    .build();
        }
        List<Addresses> list=addressRepository.findByUser(user);
        List<AddressUserResponse> lists=new ArrayList<>();
        for(Addresses a:list){
            lists.add(buildAddressResponse(a));
        }
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(RespMessage.SUCCESS.getValue())
                .errors(false)
                .data(lists)
                .build();
    }

    @Override
    public ApiResponse getAddressById(String token, Integer id) {
        String username=getUsernameFromToken();
        if(username==null||username.isEmpty()){
            return ApiResponse.builder()
                    .status(400)
                    .message("Please login to use !")
                    .errors(true)
                    .data(null)
                    .build();
        }

        Addresses addresses=addressRepository.findByIdAndUser(id,username);
        if(addresses==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Can not found address with item: "+id)
                    .errors(true)
                    .data(null)
                    .build();
        }
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(RespMessage.SUCCESS.getValue())
                .errors(false)
                .data(buildAddressResponse(addresses))
                .build();
    }

    @Override
    public ApiResponse findDefaultAddress(String token) {
        String username=getUsernameFromToken();
        if(username==null||username.isEmpty()){
            return ApiResponse.builder()
                    .status(400)
                    .message("Please login to use !")
                    .errors(true)
                    .data(null)
                    .build();
        }
        Addresses addresses=addressRepository.findByIsDefaultWithUser(username);
        if(addresses==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Can not found default address")
                    .errors(true)
                    .data(null)
                    .build();
        }
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(RespMessage.SUCCESS.getValue())
                .errors(false)
                .data(buildAddressResponse(addresses))
                .build();
    }

    @Override
    public ApiResponse delete(String token, Integer id) {
        String username=getUsernameFromToken();
        if(username==null||username.isEmpty()){
            return ApiResponse.builder()
                    .status(400)
                    .message("Please login to use !")
                    .errors(true)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        Addresses currentAddress=addressRepository.findById(id).orElse(null);
        if(currentAddress==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Can not found address with item: "+id)
                    .errors(true)
                    .data(null)
                    .build();
        }
        addressRepository.delete(currentAddress);
        if(currentAddress.getIsDefault()){
            List<Addresses> list=addressRepository.findByUser(user);
            if(list!=null&& !list.isEmpty()){
                Addresses addresses=list.get(0);
                addresses.setIsDefault(true);
                addressRepository.save(addresses);
            }
        }

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Delete address success with id: "+ id)
                .errors(false)
                .data(currentAddress)
                .build();
    }

    @Override
    public ApiResponse update(String token, Integer id, AddressUserRequest data) {
        String username=getUsernameFromToken();
        if(username==null||username.isEmpty()){
            return ApiResponse.builder()
                    .status(400)
                    .message("Please login to use !")
                    .errors(true)
                    .data(null)
                    .build();
        }
        User user=userRepository.findByUsername(username).orElse(null);
        if(user==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(RespMessage.NOT_FOUND.getValue())
                    .errors(true)
                    .data(null)
                    .build();
        }
        Addresses currentAddress=addressRepository.findById(id).orElse(null);
        if(currentAddress==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Can not found address with item: "+id)
                    .errors(true)
                    .data(null)
                    .build();
        }
        Addresses addresses=buildAddresses(data,user);
        if(addresses==null){
            return ApiResponse.builder()
                    .message(RespMessage.INVALID.getValue())
                    .status(405)
                    .errors(true)
                    .data(null)
                    .build();
        }
        currentAddress.setProvince(addresses.getProvince());
        currentAddress.setDistrict(addresses.getDistrict());
        currentAddress.setWard(addresses.getWard());
        currentAddress.setAddress(addresses.getAddress());
        currentAddress.setPhone(addresses.getPhone());
        currentAddress.setRecipient(addresses.getRecipient());
        currentAddress.setIsDefault(currentAddress.getIsDefault() || addresses.getIsDefault());
        return ApiResponse.builder()
                .message(RespMessage.SUCCESS.getValue())
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(buildAddressResponse(addressRepository.save(currentAddress)))
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
