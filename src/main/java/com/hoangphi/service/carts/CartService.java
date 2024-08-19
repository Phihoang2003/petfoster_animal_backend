package com.hoangphi.service.carts;

import com.hoangphi.request.carts.CartRequest;
import com.hoangphi.response.ApiResponse;

import java.util.List;

public interface CartService {
    ApiResponse createCart(String jwt, CartRequest cartRequest);

    ApiResponse updateCarts(String jwt, List<CartRequest> cartRequests);

    ApiResponse getCarts(String jwt);
}
