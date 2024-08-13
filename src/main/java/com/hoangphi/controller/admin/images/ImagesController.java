package com.hoangphi.controller.admin.images;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/images")
public class ImagesController {
    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getImagesByIdProduct(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.getImagesByIdProduct(id));
    }


    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> addImagesByIdProduct(@PathVariable("id") String id,
                                                            @RequestPart("images") List<MultipartFile> images) {
        return ResponseEntity.ok(imageService.addImagesByIdProduct(id, images));
    }

    @DeleteMapping("{id}/{idImage}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable("id") String id,
                                                   @PathVariable("idImage") Integer idImage) {
        return ResponseEntity.ok(imageService.deleteImage(id, idImage));
    }

    @GetMapping("/image/{fileName}")
    public byte[] getImage(@PathVariable("fileName") String fileName) {
        return imageService.getImage(fileName);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteImgs(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.deleteImgs(id));
    }
}
