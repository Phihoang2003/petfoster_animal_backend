package com.hoangphi.service.impl.order;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.constant.OrderStatus;
import com.hoangphi.entity.*;
import com.hoangphi.repository.*;
import com.hoangphi.request.order.OrderItem;
import com.hoangphi.request.order.OrderRequest;
import com.hoangphi.request.payments.VnPaymentRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.order.OrderService;
import com.hoangphi.utils.GiaoHangNhanhUtils;
import com.hoangphi.utils.VnPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final GiaoHangNhanhUtils giaoHangNhanhUltils;

    @Autowired
    private HttpServletRequest httpServletRequest;

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
        ShippingInfo shippingInfo=createShippingInfo(addresses,orderRequest);
        PaymentMethod paymentMethod=paymentMethodRepository.findById(orderRequest.getMethodId()).orElse(null);
        if(paymentMethod==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Payment method not found")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        DeliveryCompany deliveryCompany=deliveryCompanyRepository.findById(orderRequest.getDeliveryId()).orElse(null);
        if(deliveryCompany==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Delivery company not found")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        Payment payment=createPayment(orderRequest);
        Orders orders=createOrder(addresses,payment,shippingInfo);
        List<OrderDetail> orderDetails=new ArrayList<>();
        for (OrderItem orderItem : orderRequest.getOrderItems()) {
            ProductRepo productRepo = productRepoRepository.findProductRepoByIdAndSize(orderItem.getProductId(),
                    orderItem.getSize());

            if (orderItem.getQuantity() <= 0) {
                errorsMap.put("quantity", "quantity must larger than 0");
                return ApiResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("quantity must larger than 0")
                        .errors(errorsMap)
                        .build();
            }

            if (productRepo.getQuantity() < orderItem.getQuantity()) {
                errorsMap.put("quantity", "quantity are not enought, please try another one!!!");
                return ApiResponse.builder()
                        .message(HttpStatus.BAD_REQUEST.toString())
                        .errors(errorsMap)
                        .build();
            }

            OrderDetail orderDetail = this.createOrderDetail(productRepo, orderItem, orders);
            orderDetails.add(orderDetail);
            total += orderDetail.getTotal();
        }
        orders.setTotal(total);
        orders.setOrderDetails(orderDetails);
        orderRepository.save(orders);

        payment.setAmount(orders.getTotal()+shippingInfo.getShipFee());
        paymentRepository.save(payment);
        String paymentUrl;
        if (orderRequest.getMethodId() == 1) {
            orders.setStatus(OrderStatus.PLACED.getValue());

            for (OrderDetail orderDetail : orderDetails) {
                ProductRepo productRepo = orderDetail.getProductRepo();
                updateQuantity(productRepo, orderDetail.getQuantity());
            }

            if (deliveryCompany.getId() == 2) {
                ApiResponse apiResponse = giaoHangNhanhUltils.create(orders);
                if (apiResponse.getErrors().equals(true)) {
                    return apiResponse;
                }

                if (apiResponse.getStatus() == 400) {
                    return apiResponse;
                }
            }
            return ApiResponse.builder()
                    .message("Order successfully!!!")
                    .status(HttpStatus.CREATED.value())
                    .errors(false)
                    .data(orders.getId())
                    .build();
        }else{
            try{
                paymentUrl= VnPayUtils.getVnPayPayment(VnPaymentRequest.builder()
                        .amounts(payment.getAmount().intValue())
                        .idOrder(orders.getId().toString())
                        .orderInfo("Thanh toan don hang:"+orders.getId())
                        .httpServletRequest(httpServletRequest)
                        .build());
                orders.setStatus(OrderStatus.WAITING.getValue());
                orderRepository.save(orders);

            }catch(Exception e){
                return ApiResponse.builder()
                        .message("Unsupported encoding exception")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errors(true)
                        .build();
            }
            return ApiResponse.builder()
                    .message("Order successfully!!!")
                    .status(HttpStatus.OK.value())
                    .errors(false)
                    .data(paymentUrl)
                    .build();
        }
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
    private Payment createPayment(OrderRequest orderRequest){
        return paymentRepository.save(Payment.builder()
                .isPaid(false)
                .paymentMethod(paymentMethodRepository.findById(orderRequest.getMethodId()).get())
                .build());
    }
    private Orders createOrder(Addresses addresses,Payment payment,ShippingInfo shippingInfo){
        return orderRepository.save(Orders.builder()
                .user(addresses.getUser())
                .payment(payment)
                .read(false)
                .print(0)
                .shippingInfo(shippingInfo)
                .build());
    }
    private OrderDetail createOrderDetail(ProductRepo productRepo, OrderItem orderItem,Orders orders){
        return orderDetailRepository.save(OrderDetail.builder()
                .price(productRepo.getOutPrice())
                .quantity(orderItem.getQuantity())
                .total(productRepo.getOutPrice()*orderItem.getQuantity())
                .productRepo(productRepo)
                .order(orders)
                .build());
    }
    private ProductRepo updateQuantity(ProductRepo productRepo,Integer quantity){
        productRepo.setQuantity(productRepo.getQuantity()-quantity);
        return productRepoRepository.save(productRepo);
    }
}
