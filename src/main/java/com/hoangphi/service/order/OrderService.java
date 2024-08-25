package com.hoangphi.service.order;

import com.hoangphi.request.order.OrderRequest;
import com.hoangphi.request.payments.PaymentRequest;
import com.hoangphi.response.ApiResponse;

import java.util.Optional;

public interface OrderService {
    public ApiResponse order(String jwt, OrderRequest orderRequest);

    public ApiResponse payment(PaymentRequest paymentRequest);

    public ApiResponse orderDetails(String jwt, Integer id);

    public ApiResponse orderHistory(String jwt, Optional<Integer> page, Optional<String> status);
}
