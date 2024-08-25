package com.hoangphi.controller.order;

import com.hoangphi.request.order.OrderRequest;
import com.hoangphi.request.payments.PaymentRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> order(@RequestHeader("Authorization") String jwt,
                                             @Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.order(jwt, orderRequest));
    }

    @PostMapping("/payment")
    public ResponseEntity<ApiResponse> payment(@Valid @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(orderService.payment(paymentRequest));
    }

    @GetMapping("/order/history/{id}")
    public ResponseEntity<ApiResponse> orderDetails(@RequestHeader("Authorization") String jwt, @PathVariable Integer id) {
        return ResponseEntity.ok(orderService.orderDetails(jwt, id));
    }

    @GetMapping("order/history")
    public ResponseEntity<ApiResponse> ordersHistory(
            @RequestHeader("Authorization") String jwt,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("status") Optional<String> status) {
        return ResponseEntity.ok(orderService.orderHistory(jwt, page, status));
    }
}
