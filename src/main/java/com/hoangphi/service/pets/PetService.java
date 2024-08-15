package com.hoangphi.service.pets;

import com.hoangphi.entity.Pet;
import com.hoangphi.entity.User;
import com.hoangphi.request.pets.PetRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.pets.PetResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PetService {

    List<PetResponse> buildPetResponse(List<Pet> petsRaw, User user);

    ApiResponse createPet(PetRequest petRequest, List<MultipartFile> images);

    ApiResponse updatePet(String id, PetRequest petRequest);

    ApiResponse favourite(String id, String token);

    ApiResponse getAttributes();

    ApiResponse getFavorites(String token, int page);

    ApiResponse getDetailPet(String id);

    ApiResponse getPetManagement(String id);

    ApiResponse deletePet(String id);

    ApiResponse filterPets(Optional<String> name, Optional<String> typeName, Optional<String> colors, Optional<String> age,
                           Optional<Boolean> gender, Optional<String> sort, Optional<Integer> page);
}
