package com.hoangphi.controller.admin.order;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.orders_history.OrderDetailsResponse;
import com.hoangphi.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    @GetMapping("")
    public String getOrderTable(Model model) {
        List<OrderDetailsResponse> orderDetailsList = orderService.orderDetailsTable("All");
        model.addAttribute("list", orderDetailsList);
        return "orders";
    }
}
