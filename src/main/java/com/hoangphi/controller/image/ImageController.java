package com.hoangphi.controller.image;

import com.hoangphi.service.image.ImageService;
import com.hoangphi.service.impl.images.item.GetMediasItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images/")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @GetMapping("{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        byte[] imageData = imageService.getImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
    @GetMapping("/medias/{fileName}")
    public ResponseEntity<?> downloadMedias(@PathVariable String fileName) {
        GetMediasItem imageData = imageService.getMedias(fileName, "medias");

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType
                        .valueOf(imageData.getContentType() == null ? "image/png" : imageData.getContentType()))
                .body(imageData.getData());
    }
}
