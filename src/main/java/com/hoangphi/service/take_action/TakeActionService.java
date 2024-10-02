package com.hoangphi.service.take_action;

import com.hoangphi.response.ApiResponse;

import java.util.Optional;

public interface TakeActionService {
    ApiResponse homePageTakeAction();
    ApiResponse bestSellers(Optional<Integer> page);
}
