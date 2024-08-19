package com.hoangphi.controller.admin.price_change;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.price_changes.PriceChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/price-changes")
@RequiredArgsConstructor
public class PriceChangeController {
    private final PriceChangeService priceChangeService;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPriceChange(@PathVariable("id") String idProduct) {
        return ResponseEntity.ok(priceChangeService.getPriceChange(idProduct));
    }
}
