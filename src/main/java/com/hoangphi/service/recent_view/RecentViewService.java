package com.hoangphi.service.recent_view;


import com.hoangphi.response.ApiResponse;

public interface RecentViewService {
    public ApiResponse getRecentView(String jwt);
}
