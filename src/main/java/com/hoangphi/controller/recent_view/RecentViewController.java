package com.hoangphi.controller.recent_view;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.recent_view.RecentViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/recent-views")
@RequiredArgsConstructor
public class RecentViewController {
    private final RecentViewService recentViewService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getRecentView(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(recentViewService.getRecentView(jwt));
    }
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> putRecentView(@RequestHeader("Authorization") String jwt,
                                                     @PathVariable("productId") String productId) {
        return ResponseEntity.ok(recentViewService.putRecentView(jwt, productId));
    }
}
