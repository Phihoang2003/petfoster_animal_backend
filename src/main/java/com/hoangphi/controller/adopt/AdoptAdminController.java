package com.hoangphi.controller.adopt;

import com.hoangphi.request.adopts.CancelAdoptRequest;
import com.hoangphi.request.adopts.UpdatePickUpDateRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.adopt.AdoptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/adopts")
@RequiredArgsConstructor
public class AdoptAdminController {
    private final AdoptService adoptService;
    @PutMapping("/confirmed/{id}")
    public ResponseEntity<ApiResponse> doneAdoption(@PathVariable Integer id) {
        return ResponseEntity.ok(adoptService.doneAdoption(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> acceptAdoption(@PathVariable Integer id,
                                                      @Valid @RequestBody UpdatePickUpDateRequest updatePickUpDateRequest) {
        return ResponseEntity.ok(adoptService.acceptAdoption(id, updatePickUpDateRequest));
    }
    @GetMapping("/{adoptId}")
    public ResponseEntity<ApiResponse> getAdoptionOtherUser(
            @PathVariable Integer adoptId) {
        return ResponseEntity.ok(adoptService.getAdoptOtherUser(adoptId));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse> cancelAdopt(@PathVariable Integer id,
                                                   @Valid @RequestBody CancelAdoptRequest cancelAdoptRequest) {
        return ResponseEntity.ok(adoptService.cancelAdopt(id, cancelAdoptRequest));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> filterAdopts(
            @RequestParam("name") Optional<String> name,
            @RequestParam("petName") Optional<String> petName,
            @RequestParam("status") Optional<String> status,
            @RequestParam("registerStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> registerStart,
            @RequestParam("registerEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> registerEnd,
            @RequestParam("adoptStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> adoptStart,
            @RequestParam("adoptEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> adoptEnd,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("page") Optional<Integer> page) {
        return ResponseEntity.ok(adoptService.filterAdopts(name, petName, status, registerStart, registerEnd,
                adoptStart, adoptEnd, sort, page));
    }

}
