package com.hoangphi.service.adopt;

import com.hoangphi.request.adopts.AdoptsRequest;
import com.hoangphi.request.adopts.CancelAdoptRequest;
import com.hoangphi.request.adopts.UpdatePickUpDateRequest;
import com.hoangphi.response.ApiResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public interface AdoptService {
    ApiResponse adopt(String jwt, AdoptsRequest adoptsRequest);

    ApiResponse doneAdoption(Integer id);

    ApiResponse acceptAdoption(Integer id, UpdatePickUpDateRequest updatePickUpDateRequest);

    ApiResponse getAdopts(String jwt, Optional<Integer> page, Optional<String> status);

    ApiResponse getAdoptOtherUser(Integer adoptId);

    ApiResponse cancelAdopt(Integer id, CancelAdoptRequest cancelAdoptRequest);

    ApiResponse cancelAdoptByUser(Integer id, String jwt, CancelAdoptRequest cancelAdoptRequest);

    ApiResponse filterAdopts(
            Optional<String> name,
            Optional<String> petName,
            Optional<String> status,
            Optional<LocalDate> registerStart,
            Optional<LocalDate> registerEnd,
            Optional<LocalDate> adoptStart,
            Optional<LocalDate> adoptEnd,
            Optional<String> sort,
            Optional<Integer> page);

    ApiResponse reports();


}