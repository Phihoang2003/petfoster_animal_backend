package com.hoangphi.service.impl.order;

import com.hoangphi.constant.Constant;
import com.hoangphi.constant.OrderStatus;
import com.hoangphi.entity.Orders;
import com.hoangphi.entity.Payment;
import com.hoangphi.repository.OrderRepository;
import com.hoangphi.repository.PaymentRepository;
import com.hoangphi.repository.ProductRepoRepository;
import com.hoangphi.request.order.UpdateStatusRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.order.AdminOrderService;
import com.hoangphi.utils.FormatUtils;
import com.hoangphi.utils.GiaoHangNhanhUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {
    private final OrderRepository orderRepository;
    private final FormatUtils formatUtils;
    private final GiaoHangNhanhUtils giaoHangNhanhUtils;
    private final PaymentRepository paymentRepository;
    private final OrderServiceImpl orderServiceImpl;
    private final ProductRepoRepository productRepoRepository;
    @Override
    public ApiResponse updateOrderStatus(Integer id, UpdateStatusRequest updateStatusRequest) {
        Orders orders=orderRepository.findById(id).orElse(null);
        if(orders==null){
            return ApiResponse.builder()
                    .message("order not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        if( orders.getStatus().equalsIgnoreCase(OrderStatus.DELIVERED.getValue())||
                orders.getStatus().equalsIgnoreCase(OrderStatus.CANCELLED_BY_CUSTOMER.getValue())||
                orders.getStatus().equalsIgnoreCase(OrderStatus.CANCELLED_BY_ADMIN.getValue())){
            return ApiResponse.builder()
                    .message("Cannot update the order has been delivered or cancelled")
                    .status(HttpStatus.FAILED_DEPENDENCY.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        String updateStatus;
        try {
            updateStatus = OrderStatus.valueOf(updateStatusRequest.getStatus()).getValue();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .message(updateStatusRequest.getStatus() + " doesn't exists in the enum")
                    .status(404)
                    .errors(updateStatusRequest.getStatus() + " doesn't exists in the enum")
                    .build();
        }
        if (updateStatus.equalsIgnoreCase(OrderStatus.PLACED.getValue())) {
            return ApiResponse.builder()
                    .message("Cannot update back the status")
                    .status(HttpStatus.CONFLICT.value())
                    .errors("Cannot update back the status")
                    .build();
        }
        if (updateStatus.equalsIgnoreCase(OrderStatus.DELIVERED.getValue())
                && !orders.getStatus().equalsIgnoreCase(OrderStatus.SHIPPING.getValue())) {
            return ApiResponse.builder()
                    .message("Please update this order to Shipping status first")
                    .status(HttpStatus.CONFLICT.value())
                    .errors("Cannot jump update the status")
                    .build();
        }
        if (updateStatus.equalsIgnoreCase(OrderStatus.CANCELLED_BY_CUSTOMER.getValue())) {
            return ApiResponse.builder()
                    .message("Please choose Cancelled By Admin")
                    .status(HttpStatus.CONFLICT.value())
                    .errors("Admin cannot choose Cancelled By Customer")
                    .build();
        }
        orders.setStatus(updateStatus);
        orders.setDescriptions(updateStatusRequest.getReason() != null ? updateStatusRequest.getReason() : "");
        orderRepository.save(orders);
        Payment payment = orders.getPayment();
        if(updateStatus.equalsIgnoreCase(OrderStatus.DELIVERED.getValue())){
            payment.setIsPaid(true);
            payment.setPayAt(LocalDate.now());
            paymentRepository.save(payment);
        }
        if (updateStatus.equalsIgnoreCase(OrderStatus.CANCELLED_BY_ADMIN.getValue()) && orders.getGhnCode() != null) {

            List<String> order_codes = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();

            order_codes.add(orders.getGhnCode());
            HttpEntity<Map<String, Object>> request = giaoHangNhanhUtils.createRequest("order_codes", order_codes);
            ResponseEntity<String> response = restTemplate.postForEntity(Constant.GHN_CANCEL, request, String.class);

            // return quantity
            orders.getOrderDetails().forEach(orderDetail -> {
                orderServiceImpl.returnQuantity(orderDetail.getProductRepo(), orderDetail.getQuantity());
            });
        }
        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .build();
    }

    @Override
    public ApiResponse updateReadForOrder(Integer id) {
        return null;
    }

    @Override
    public ApiResponse updatePrintForOrder(Integer id) {
        return null;
    }

}
