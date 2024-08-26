package com.hoangphi.controller.admin.order;

import com.hoangphi.request.order.UpdateStatusRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.order.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final AdminOrderService adminOrderService;
    @PostMapping("/status/{id}")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Integer id,
                                                         @RequestBody UpdateStatusRequest updateStatusRequest) {
        return ResponseEntity.ok(adminOrderService.updateOrderStatus(id, updateStatusRequest));
    }
}
