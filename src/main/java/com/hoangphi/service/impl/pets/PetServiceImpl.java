package com.hoangphi.service.impl.pets;

import com.hoangphi.constant.PetStatus;
import com.hoangphi.entity.*;
import com.hoangphi.repository.*;
import com.hoangphi.request.pets.PetRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.pets.PetDetailResponse;
import com.hoangphi.service.pets.PetService;
import com.hoangphi.service.user.UserService;
import com.hoangphi.utils.ImageUtils;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetBreedRepository petBreedRepository;
    private final PetRepository petRepository;
    private final PetImageRepository petImageRepository;

    private final AdoptRepository adoptRepository;
    private final PortUtils portUltils;
    private final UserService userService;
    private final FavouriteRepository favouriteRepository;
    @Override
    public ApiResponse createPet(PetRequest petRequest, List<MultipartFile> images) {
        PetBreed petBreed = petBreedRepository.findById(petRequest.getBreed()).orElse(null);
        List<Pet> pets = petRepository.findAll();
        if(petBreed==null){
            return ApiResponse.builder()
                    .message("Breed not found!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        if(images.isEmpty()){
            return ApiResponse.builder()
                    .message("Images can't be empty!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        String newProductId;
        if (pets.isEmpty()) {
            newProductId = "P0001"; // Ví dụ: ID mặc định nếu không có sản phẩm nào
        } else {
            newProductId = getNextId(pets.get(pets.size() - 1).getPetId());
        }
        Pet pet = Pet.builder().petId(newProductId)
                .petName(petRequest.getName())
                .petBreed(petBreed)
                .sex(petRequest.getSex())
                .petColor(petRequest.getColor())
                .age(petRequest.getSize())
                .isSpay(petRequest.getIsSpay())
                .descriptions(petRequest.getDescription())
                .fosterAt(petRequest.getFosterAt())
                .adoptStatus(petRequest.getStatus())
                .build();
        petRepository.save(pet);
        //get 4 images
        if (images.size() > 4) {
            images = images.subList(0, 4);
        }
        List<PetImgs> newImages=images.stream().map(image->{
            try{
                File newFile= ImageUtils.createFileImage();
                image.transferTo(new File(newFile.getAbsolutePath()));
                return PetImgs.builder()
                        .nameImg(newFile.getName())
                        .pet(pet)
                        .build();
            }catch(Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }).toList();
        pet.setImgs(newImages);
        petImageRepository.saveAll(newImages);
        return ApiResponse.builder()
                .status(200)
                .message("Create Pet Successfully!!!")
                .errors(false)
                .data(buildPetResponse(pet))
                .build();


    }

    @Override
    public ApiResponse updatePet(String id, PetRequest petRequest) {
        PetBreed petBreed = petBreedRepository.findById(petRequest.getBreed()).orElse(null);
        if(petBreed==null){
            return ApiResponse.builder()
                    .message("Breed not found!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        Pet pet=petRepository.findById(id).orElse(null);
        if(pet==null){
            return ApiResponse.builder()
                    .message("Pet not found!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        pet.setAge(petRequest.getSize());
        pet.setDescriptions(petRequest.getDescription());
        pet.setFosterAt(petRequest.getFosterAt());
        pet.setIsSpay(petRequest.getIsSpay());
        pet.setPetBreed(petBreed);
        pet.setPetColor(petRequest.getColor());
        pet.setPetName(petRequest.getName());
        pet.setSex(petRequest.getSex());
        pet.setAdoptStatus(petRequest.getStatus());
        petRepository.save(pet);

        return ApiResponse.builder()
                .status(200)
                .message("Update Successfully!!!")
                .errors(false)
                .data(buildPetResponse(pet))
                .build();
    }

    @Override
    public ApiResponse favourite(String id, String token) {
        if(id.isEmpty()){
            return ApiResponse.builder()
                    .message("Pet ID can't be empty!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        User user=userService.getUserFromToken(token);
        Pet pet=petRepository.findById(id).orElse(null);
        if(user==null||pet==null){
            return ApiResponse.builder()
                    .message("User or Pet not found!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        Favourite isFavourite=favouriteRepository.existByUserAndPet(user.getId(),id);
        Favourite favouriteResponse=isFavourite;
        if(isFavourite==null){
            favouriteResponse=Favourite.builder()
                    .pet(pet)
                    .user(user)
                    .build();
            favouriteRepository.save(favouriteResponse);
        }else{
            favouriteRepository.delete(isFavourite);
        }
        return ApiResponse.builder()
                .data(favouriteResponse)
                .status(HttpStatus.OK.value())
                .errors(false)
                .message(isFavourite == null ? "Favourite Successfully" : "Unfavourite Successfully")
                .build();

    }

    public PetDetailResponse buildPetResponse(Pet pet){
        Integer fosterDate= (int)ChronoUnit.DAYS.between(pet.getFosterAt(), LocalDateTime.now());
        boolean canAdopt=isCanAdopt(pet,null);
        List<String> images = pet.getImgs().stream().map(image -> {
            return portUltils.getUrlImage(image.getNameImg());
        }).toList();

        return PetDetailResponse.builder()
                .id(pet.getPetId())
                .breed(pet.getPetBreed().getBreedName())
                .name(pet.getPetName())
                .image(portUltils.getUrlImage(pet.getImgs()
                        .stream()
                        .findFirst()
                        .map(PetImgs::getNameImg)
                        .orElse(null)))
                .description(pet.getDescriptions())
                .fosterDate(fosterDate)
                .size(pet.getAge())
                .sex(pet.getSex() ? "male" : "female")
                .type(pet.getPetBreed().getPetType().getName())
                .like(false)
                .fostered(pet.getFosterAt())
                .sterilization(pet.getIsSpay() ? "sterilized" : "none")
                .images(images)
                .color(pet.getPetColor())
                .canAdopt(canAdopt)
                .status(pet.getAdoptStatus())
                .build();

    }
    public Boolean isCanAdopt(Pet pet, User user){
        if (pet.getAdoptStatus().equalsIgnoreCase(PetStatus.SICK.getValue())
                || pet.getAdoptStatus().equalsIgnoreCase(PetStatus.DECEASED.getValue())) {
            return false;
        }
        if (user == null) {
            return adoptRepository.exitsAdopted(pet.getPetId())==null;
        } else {
            return adoptRepository.existsByPetAndUser(pet.getPetId(), user.getId())==null
                    && adoptRepository.exitsAdopted(pet.getPetId())==null;
        }

    }
    public String getNextId(String lastId){
        String nextId="";
        String first=lastId.substring(0,2);
        String last=lastId.substring(2);
        int number=Integer.parseInt(last);
        double log=Math.log10(number);

        if(log<1){
            nextId=first+"000"+ ++number;
        }else if(log<2){
            nextId=first+"00"+ ++number;
        }else if(log<3){
            nextId=first+"0"+ ++number;
        }else{
            nextId=first+ ++number;
        }
        return nextId;
    }
}
