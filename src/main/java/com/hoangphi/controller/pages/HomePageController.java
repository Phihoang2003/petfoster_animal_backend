package com.hoangphi.controller.pages;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.pages.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home-pages")
@RequiredArgsConstructor
public class HomePageController {
    private final HomePageService homePageService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getMethodName() {

        return ResponseEntity.ok(homePageService.homepage());
    }
}
