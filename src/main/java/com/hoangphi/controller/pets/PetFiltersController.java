package com.hoangphi.controller.pets;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.pets.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetFiltersController {
    private final PetService petService;
    @GetMapping("/attributes")
    public ResponseEntity<ApiResponse> getAttributes() {
        return ResponseEntity.ok(petService.getAttributes());
    }
    @GetMapping("")
    public ResponseEntity<ApiResponse> filterPets(
            @RequestParam("name") Optional<String> name,
            @RequestParam("typeName") Optional<String> typeName,
            @RequestParam("colors") Optional<String> colors,
            @RequestParam("age") Optional<String> age,
            @RequestParam("gender") Optional<Boolean> gender,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("page") Optional<Integer> page) {
        return ResponseEntity.ok(petService.filterPets(name, typeName, colors, age, gender, sort, page));
    }

}
