package com.hoangphi.controller.admin.order;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.orders_history.OrderDetailsResponse;
import com.hoangphi.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse> getOrderTable(Model model, @PathVariable("username") String username) {
        List<OrderDetailsResponse> orderDetailsList = orderService.orderDetailsTable(username);
        model.addAttribute("list", orderDetailsList);
        return ResponseEntity.ok(ApiResponse.builder().data(model).build());
    }
}
