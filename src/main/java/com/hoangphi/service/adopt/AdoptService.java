package com.hoangphi.service.adopt;

import com.hoangphi.request.adopts.AdoptsRequest;
import com.hoangphi.request.adopts.UpdatePickUpDateRequest;
import com.hoangphi.response.ApiResponse;

public interface AdoptService {
    ApiResponse adopt(String jwt, AdoptsRequest adoptsRequest);

    ApiResponse doneAdoption(Integer id);

    ApiResponse acceptAdoption(Integer id, UpdatePickUpDateRequest updatePickUpDateRequest);
}
