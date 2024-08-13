package com.hoangphi.service.admin.images;

import com.hoangphi.entity.Imgs;
import com.hoangphi.response.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    byte[] getImage(String fileName);
    ApiResponse deleteImgs(String id);

    ApiResponse getImagesByIdProduct(String id);

    ApiResponse addImagesByIdProduct(String id, List<MultipartFile> images);

    ApiResponse deleteImage(String id, Integer idImage);
}
