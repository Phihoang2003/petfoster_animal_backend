package com.hoangphi.controller.admin.order;

import com.hoangphi.response.orders_history.OrderDetailsResponse;
import com.hoangphi.service.order.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderPrintInvoiceController {
    private final OrderService orderService;
    @GetMapping("/print/{id}")
    public String printInvoice(Model model, @PathVariable("id") Integer id, HttpServletResponse response) {
        OrderDetailsResponse orderDetailsResponse = orderService.printInvoice(id);

        response.setHeader("X-Frame-Options", "ALLOWALL");
        response.setHeader("TOKEN", "123");

        model.addAttribute("data", orderDetailsResponse);
        return "invoice";
    }
}
