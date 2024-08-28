package com.hoangphi.controller.admin.donate;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.donate.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/donates")
@RequiredArgsConstructor
public class ManagementDonate {
    private final DonateService donateService;

    @GetMapping("/report")
    public ResponseEntity<ApiResponse> report() {
        return ResponseEntity.ok(donateService.report());
    }
}
