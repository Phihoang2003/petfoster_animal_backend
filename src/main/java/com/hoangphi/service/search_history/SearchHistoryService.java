package com.hoangphi.service.search_history;

import com.hoangphi.response.ApiResponse;

public interface SearchHistoryService {
    ApiResponse getSearchHistory(String jwt);
    ApiResponse updateSearchHistory(String jwt, String keyword);
    ApiResponse deleteSearchHistory(String jwt, String keyword);
}
