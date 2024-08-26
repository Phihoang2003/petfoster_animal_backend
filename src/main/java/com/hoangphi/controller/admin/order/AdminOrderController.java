package com.hoangphi.controller.admin.order;

import com.hoangphi.request.order.UpdateStatusRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.order.OrderFilterService;
import com.hoangphi.service.order.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final AdminOrderService adminOrderService;
    private final OrderFilterService orderFilterService;
    @PostMapping("/status/{id}")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Integer id,
                                                         @RequestBody UpdateStatusRequest updateStatusRequest) {
        return ResponseEntity.ok(adminOrderService.updateOrderStatus(id, updateStatusRequest));
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<ApiResponse> updateReadForOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(adminOrderService.updateReadForOrder(id));
    }

    @PutMapping("print/{id}")
    public ResponseEntity<ApiResponse> updatePrintForOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(adminOrderService.updatePrintForOrder(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse> filterOrders(
            @RequestParam("username") Optional<String> username,
            @RequestParam("orderId") Optional<Integer> orderId,
            @RequestParam("status") Optional<String> status,
            @RequestParam("minDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> minDate,
            @RequestParam("maxDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> maxDate,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("read") Optional<Boolean> read) {
        return ResponseEntity
                .ok(orderFilterService.filterOrders(username, orderId, status, minDate, maxDate, sort, page, read));
    }
}
