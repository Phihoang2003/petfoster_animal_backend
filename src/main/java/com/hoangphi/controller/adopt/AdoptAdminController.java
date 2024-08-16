package com.hoangphi.controller.adopt;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.adopt.AdoptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/adopts")
@RequiredArgsConstructor
public class AdoptAdminController {
    private final AdoptService adoptService;
    @PutMapping("/confirmed/{id}")
    public ResponseEntity<ApiResponse> doneAdoption(@PathVariable Integer id) {
        return ResponseEntity.ok(adoptService.doneAdoption(id));
    }
}
