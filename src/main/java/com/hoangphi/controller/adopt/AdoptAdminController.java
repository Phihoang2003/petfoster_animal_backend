package com.hoangphi.controller.adopt;

import com.hoangphi.request.adopts.CancelAdoptRequest;
import com.hoangphi.request.adopts.UpdatePickUpDateRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.adopt.AdoptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
