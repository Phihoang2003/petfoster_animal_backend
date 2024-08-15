package com.hoangphi.controller.pets;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.pets.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
    @PutMapping("/favourite/{id}")
    public ResponseEntity<ApiResponse> favorite(@PathVariable("id") String id,
                                                @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(petService.favourite(id, token));
    }

    @GetMapping("/favourites")
    public ResponseEntity<ApiResponse> getFavorites(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return ResponseEntity.ok(petService.getFavorites(token, page));
    }

}
