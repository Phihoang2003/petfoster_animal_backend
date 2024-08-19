package com.hoangphi.controller.cart;

import com.hoangphi.request.carts.CartRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.carts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<ApiResponse> createCart(@RequestHeader("Authorization") String jwt,
                                                  @RequestBody CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.createCart(jwt, cartRequest));
    }

    @PutMapping("/carts")
    public ResponseEntity<ApiResponse> updateCarts(@RequestHeader("Authorization") String jwt,
                                                   @RequestBody List<CartRequest> cartRequests) {
        return ResponseEntity.ok(cartService.updateCarts(jwt, cartRequests));
    }
}
