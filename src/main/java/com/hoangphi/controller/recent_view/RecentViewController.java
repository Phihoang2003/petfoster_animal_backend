package com.hoangphi.controller.recent_view;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.recent_view.RecentViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/recent-views")
@RequiredArgsConstructor
public class RecentViewController {
    private final RecentViewService recentViewService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getRecentView(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(recentViewService.getRecentView(jwt));
    }
}
