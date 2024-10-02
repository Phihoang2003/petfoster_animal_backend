package com.hoangphi.controller.pages;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.take_action.TakeActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/take-action")
@RequiredArgsConstructor
public class TakeActionController {
    private final TakeActionService takeActionService;;
    @GetMapping()
    public ResponseEntity<ApiResponse> homePageTakeAction() {

        return ResponseEntity.ok(takeActionService.homePageTakeAction());

    }

    @GetMapping("/best-sellers")
    public ResponseEntity<ApiResponse> getBestSellers(@RequestParam("page") Optional<Integer> page) {
        return ResponseEntity.ok(takeActionService.bestSellers(page));

    }
}
