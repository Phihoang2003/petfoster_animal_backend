package com.hoangphi.controller.search_history;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.search_history.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/search-histories")
@RequiredArgsConstructor
public class SearchHistoryController {
    private final SearchHistoryService searchHistoryService;
    @GetMapping("")
    public ResponseEntity<ApiResponse> getSearchHistory(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(searchHistoryService.getSearchHistory(jwt));
    }

    @PutMapping("")
    public ResponseEntity<ApiResponse> updateSearchHistory(@RequestHeader("Authorization") String jwt,
                                                           @RequestParam String keyword) {
        return ResponseEntity.ok(searchHistoryService.updateSearchHistory(jwt, keyword));
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponse> deleteSearchHistory(@RequestHeader("Authorization") String jwt,
                                                           @RequestParam String keyword) {
        return ResponseEntity.ok(searchHistoryService.deleteSearchHistory(jwt, keyword));
    }
}
