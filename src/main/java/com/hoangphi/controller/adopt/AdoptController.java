package com.hoangphi.controller.adopt;

import com.hoangphi.request.adopts.AdoptsRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.adopt.AdoptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/adopts")
public class AdoptController {
    private final AdoptService adoptService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> adopt(@RequestHeader("Authorization") String jwt,
                                             @Valid @RequestBody AdoptsRequest adoptsRequest) {
        return ResponseEntity.ok(adoptService.adopt(jwt, adoptsRequest));
    }
}
