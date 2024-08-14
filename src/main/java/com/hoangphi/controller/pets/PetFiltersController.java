package com.hoangphi.controller.pets;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.pets.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetFiltersController {
    private final PetService petService;
    @GetMapping("/attributes")
    public ResponseEntity<ApiResponse> getAttributes() {
        return ResponseEntity.ok(petService.getAttributes());
    }
}
