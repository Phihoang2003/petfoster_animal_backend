package com.hoangphi.service.impl.order;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.entity.Addresses;
import com.hoangphi.entity.ShippingInfo;
import com.hoangphi.entity.User;
import com.hoangphi.repository.*;
import com.hoangphi.request.order.OrderRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final JwtProvider jwtProvider;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ProductRepoRepository productRepoRepository;
    private final ShippingInfoRepository shippingInfoRepository;
    private final DeliveryCompanyRepository deliveryCompanyRepository;
    @Override
    public ApiResponse order(String jwt, OrderRequest orderRequest) {
        Double total=0.0;
        Map<String,String> errorsMap=new HashMap< >();
        User user=userRepository.findByUsername(jwtProvider.getUsernameFromToken(jwt)).orElse(null);
        if(user==null){
            errorsMap.put("user","User not found");
            return ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthenticated")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        Addresses addresses=addressRepository.findById(orderRequest.getAddressId()).orElse(null);
        if(addresses==null){
            errorsMap.put("address","Address not found");
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Address not found")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        if(!user.getAddresses().contains(addresses)){
            errorsMap.put("address","This address not found in address list of this user");
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("This address not found in address list of this user")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }


        return null;
    }
    private ShippingInfo createShippingInfo(Addresses addresses,OrderRequest orderRequest){
        return shippingInfoRepository.save(ShippingInfo.builder()
                .fullName(addresses.getRecipient())
                .district(addresses.getDistrict())
                .province(addresses.getProvince())
                .ward(addresses.getWard())
                .address(addresses.getAddress())
                .phone(addresses.getPhone())
                .shipFee(orderRequest.getShip())
                .deliveryCompany(deliveryCompanyRepository.findById(orderRequest.getDeliveryId()).get())
                .build());
    }
}
