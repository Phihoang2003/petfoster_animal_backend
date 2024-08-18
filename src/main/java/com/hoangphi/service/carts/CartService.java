package com.hoangphi.service.carts;

import com.hoangphi.request.carts.CartRequest;
import com.hoangphi.response.ApiResponse;

public interface CartService {
    ApiResponse createCart(String jwt, CartRequest cartRequest);
}
