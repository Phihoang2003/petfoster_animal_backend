package com.hoangphi.controller.admin.pet;

import com.hoangphi.request.pets.PetRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.pets.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> createPet(@Valid @ModelAttribute PetRequest createPetRequest,
                                                 @RequestPart List<MultipartFile> images) {
        return ResponseEntity.ok(petService.createPet(createPetRequest, images));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePet(@PathVariable("id") String id,@Valid
    @RequestBody PetRequest petRequest) {
        return ResponseEntity.ok(petService.updatePet(id, petRequest));
    }
}
