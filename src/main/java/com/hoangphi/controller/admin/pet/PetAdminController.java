package com.hoangphi.controller.admin.pet;

import com.hoangphi.request.pets.PetRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.pets.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/pets")
@RequiredArgsConstructor
public class PetAdminController {
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
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPetManagement(@PathVariable String id) {
        return ResponseEntity.ok(petService.getPetManagement(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePet(@PathVariable("id") String id) {
        return ResponseEntity.ok(petService.deletePet(id));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> filterAdminPets(
            @RequestParam("name") Optional<String> name,
            @RequestParam("typeName") Optional<String> typeName,
            @RequestParam("colors") Optional<String> colors,
            @RequestParam("age") Optional<String> age,
            @RequestParam("gender") Optional<Boolean> gender,
            @RequestParam("status") Optional<String> status,
            @RequestParam("minDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> minDate,
            @RequestParam("maxDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> maxDate,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("page") Optional<Integer> page) {
        return ResponseEntity.ok(
                petService.filterAdminPets(name, typeName, colors, age, gender, status, minDate, maxDate, sort, page));
    }

}
