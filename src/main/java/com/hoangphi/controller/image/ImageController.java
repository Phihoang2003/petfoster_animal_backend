package com.hoangphi.controller.image;

import com.hoangphi.service.image.ImageServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ImageController {
    private final ImageServiceUtils imageService;
    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        String preSignedUrl = imageService.getImage(fileName);
        return ResponseEntity.ok(preSignedUrl);
    }
//    @GetMapping("/medias/{fileName}")
//    public ResponseEntity<?> downloadMedias(@PathVariable String fileName) {
//        GetMediasItem imageData = imageService.getMedias(fileName, "medias");
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType
//                        .valueOf(imageData.getContentType() == null ? "image/png" : imageData.getContentType()))
//                .body(imageData.getData());
//    }
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException, InterruptedException {
        List<String> fileUrls = imageService.uploadFiles(files);
        return ResponseEntity.ok(fileUrls);
    }
}
