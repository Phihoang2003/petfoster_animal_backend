package com.hoangphi.controller.admin.donate;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.donate.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/donates")
@RequiredArgsConstructor
public class ManagementDonate {
    private final DonateService donateService;

    @GetMapping("/report")
    public ResponseEntity<ApiResponse> report() {
        return ResponseEntity.ok(donateService.report());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> filter(@RequestParam("search") Optional<String> search,
                                              @RequestParam("minDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> minDate,
                                              @RequestParam("maxDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> maxDate,
                                              @RequestParam("sort") Optional<String> sort,
                                              @RequestParam("page") Optional<Integer> page) {
        return ResponseEntity.ok(donateService.filterTransaction(search, minDate, maxDate, sort, page));
    }


}
