package com.hoangphi.controller.pages;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.pets.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetDetailPageController {
    private final PetService petService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDetailPet(@PathVariable("id") String id) {
        return ResponseEntity.ok(petService.getDetailPet(id));
    }
}
