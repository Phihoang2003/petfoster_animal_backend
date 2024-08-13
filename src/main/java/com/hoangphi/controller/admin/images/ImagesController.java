package com.hoangphi.controller.admin.images;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/images")
public class ImagesController {
    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getImagesByIdProduct(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.getImagesByIdProduct(id));
    }
}
