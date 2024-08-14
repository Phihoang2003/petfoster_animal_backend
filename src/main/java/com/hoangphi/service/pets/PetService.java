package com.hoangphi.service.pets;

import com.hoangphi.request.pets.PetRequest;
import com.hoangphi.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PetService {
    ApiResponse createPet(PetRequest petRequest, List<MultipartFile> images);

    ApiResponse updatePet(String id, PetRequest petRequest);

    ApiResponse favourite(String id, String token);
}
