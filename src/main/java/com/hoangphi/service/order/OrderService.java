package com.hoangphi.service.order;

import com.hoangphi.request.order.OrderRequest;
import com.hoangphi.response.ApiResponse;

public interface OrderService {
    public ApiResponse order(String jwt, OrderRequest orderRequest);
}
