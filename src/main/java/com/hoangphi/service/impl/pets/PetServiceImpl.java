package com.hoangphi.service.impl.pets;

import com.hoangphi.constant.PetStatus;
import com.hoangphi.entity.*;
import com.hoangphi.repository.*;
import com.hoangphi.request.pets.PetRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.common.PaginationResponse;
import com.hoangphi.response.pets.PetAttributeResponse;
import com.hoangphi.response.pets.PetAttributesResponse;
import com.hoangphi.response.pets.PetDetailResponse;
import com.hoangphi.service.pets.PetService;
import com.hoangphi.service.user.UserService;
import com.hoangphi.utils.ImageUtils;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final PetTypeRepository petTypeRepository;
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

    @Override
    public ApiResponse getAttributes() {
        List<Pet> pets=petRepository.findAll();
        List<PetAttributeResponse> colors=pets.stream().map(pet->{
            return new PetAttributeResponse(pet.getPetColor().toLowerCase(),pet.getPetColor());
        }).distinct().toList();
        List<PetAttributeResponse> states=pets
                .stream()
                .map(pet->{
            String shortStatus=switch (pet.getAdoptStatus().toLowerCase()){
                case "available"->"available";
                case "fostered"->"fostered";
                case "adopted"->"adopted";
                default -> "unknown";
            };
            return new PetAttributeResponse(shortStatus,pet.getAdoptStatus());
        }).distinct().toList();
        List<PetAttributeResponse> breeds=petBreedRepository
                .findAll()
                .stream()
                .map(petBreed-> new PetAttributeResponse(petBreed.getBreedId(),petBreed.getBreedName()))
                .distinct()
                .toList();
        List<PetAttributeResponse> types=petTypeRepository.findAll()
                .stream()
                .map(petType -> new PetAttributeResponse(petType.getId(),petType.getName()))
                .distinct()
                .toList();
        PetAttributesResponse attributes = PetAttributesResponse.builder()
                .breeds(breeds)
                .colors(colors)
                .states(states)
                .typies(types)
                .build();

        // Trả về kết quả dưới dạng ApiResponse
        return ApiResponse.builder()
                .message("Get attributes Successfully!")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(attributes)
                .build();
    }

    @Override
    public ApiResponse getFavorites(String token, int page) {
        User user=userService.getUserFromToken(token);
        List<Pet> list=petRepository.getFavoritePets(user.getId());
        int pageSize = 10;
        int totalPages = (list.size() + pageSize - 1) / pageSize;

        if (page >= totalPages) {
            return ApiResponse.builder()
                    .status(HttpStatus.NO_CONTENT.value())
                    .message("Page is not exist!!!")
                    .errors(false)
                    .data(new ArrayList<>())
                    .build();
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), list.size());
        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .status(200)
                    .message("Successfully!!!")
                    .errors(false)
                    .data(PaginationResponse.builder().data(list).pages(0).build())
                    .build();
        }

        List<Pet> visiblePets = list.subList(startIndex, endIndex);
        List<PetDetailResponse> pets = new ArrayList<>();
        visiblePets.forEach(pet -> pets.add(this.buildPetResponse(pet, user)));

        return ApiResponse.builder()
                .status(200)
                .message("Successfully!!!")
                .errors(false)
                .data(PaginationResponse.builder().data(pets).pages(totalPages).build())
                .build();

    }

    public PetDetailResponse buildPetResponse(Pet pet){
        return buildPetResponse(pet,null);
    }
    public PetDetailResponse buildPetResponse(Pet pet,User user){
        Integer fosterDate= (int)ChronoUnit.DAYS.between(pet.getFosterAt(), LocalDateTime.now());
        boolean canAdopt=isCanAdopt(pet,null);
        boolean liked = user != null && favouriteRepository.existByUserAndPet(user.getId(), pet.getPetId()) != null;
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
                .like(liked)
                .fostered(pet.getFosterAt())
                .sterilization(pet.getIsSpay() ? "sterilized" : "none")
                .images(images)
                .color(pet.getPetColor())
                .canAdopt(canAdopt)
                .status(pet.getAdoptStatus())
                .build();
    }
    public Boolean isCanAdopt(Pet pet, User user){
        if (pet.getAdoptStatus().equalsIgnoreCase(PetStatus.FOSTERED.getValue())||
                pet.getAdoptStatus().equalsIgnoreCase(PetStatus.ADOPTED.getValue())) {
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
        String first=lastId.substring(0,1);
        String last=lastId.substring(1);
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
