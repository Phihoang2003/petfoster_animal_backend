package com.hoangphi.controller.image;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user/messages")
@RequiredArgsConstructor
public class ImageMessageController {

   private final ImageService imagesService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> uploadImages(@RequestPart List<MultipartFile> images) {
        return ResponseEntity.ok(imagesService.uploadImages(images));
    }
}
