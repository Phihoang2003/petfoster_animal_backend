package com.hoangphi.service.impl.pets;

import com.hoangphi.constant.PetStatus;
import com.hoangphi.entity.*;
import com.hoangphi.repository.*;
import com.hoangphi.request.pets.PetRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.common.PaginationResponse;
import com.hoangphi.response.pages.PetDetailPageResponse;
import com.hoangphi.response.pets.*;
import com.hoangphi.service.image.ImageServiceUtils;
import com.hoangphi.service.pets.PetService;
import com.hoangphi.service.user.UserService;
import com.hoangphi.utils.ImageUtils;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetBreedRepository petBreedRepository;
    private final PetRepository petRepository;
    private final PetImageRepository petImageRepository;
    private final ImageServiceUtils imageServiceUtils;
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
        List<String> imagesFilter = imageServiceUtils.uploadFiles(images);
        List<PetImgs> newImages=imagesFilter.stream().map(image->{
            try{
//                File newFile= ImageUtils.createFileImage();
//                image.transferTo(new File(newFile.getAbsolutePath()));
                return PetImgs.builder()
                        .nameImg(image)
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
                    .status(HttpStatus.NO_CONTENT.value())  // Trả về mã trạng thái 204 (No Content)
                    .message("No data available!!!")
                    .errors(false)
                    .data(PaginationResponse.builder().data(Collections.emptyList()).pages(0).build())
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

    @Override
    public ApiResponse getDetailPet(String id) {
        if(id.isEmpty()){
            return ApiResponse.builder()
                    .message("Pet ID can't be empty!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getHeader("Authorization");
        if (token == null) {
            return ApiResponse.builder()
                    .message("Token can't be empty!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .build();
        }
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            return ApiResponse.builder()
                    .message("Pet not found!")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        List<Pet> otherPetsRaw=petRepository.findByPetStyleAndIgnorePetId(pet.getPetBreed().getBreedId()
                ,pet.getAge(),pet.getPetId());

        if(!token.isEmpty()){

            User user=userService.getUserFromToken(token);
            if(user==null){
                return ApiResponse.builder()
                        .message("User not found!")
                        .data(null)
                        .errors(true)
                        .status(HttpStatus.NOT_FOUND.value())
                        .build();
            }
            PetDetailResponse petResponse=buildPetResponse(pet,user);
            List<PetResponse> otherPets=buildPetResponse(otherPetsRaw,user);

            PetDetailPageResponse petPageReponse=PetDetailPageResponse.builder()
                    .pet(petResponse)
                    .others(otherPets)
                    .build();
            return ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Successfully!!!")
                    .errors(false)
                    .data(petPageReponse)
                    .build();

        }else{
            PetDetailResponse petResponse=buildPetResponse(pet);
            List<PetResponse> otherPets=buildPetResponse(otherPetsRaw,null);

            PetDetailPageResponse petPageReponse=PetDetailPageResponse.builder()
                    .pet(petResponse)
                    .others(otherPets)
                    .build();
            return ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Successfully!!!")
                    .errors(false)
                    .data(petPageReponse)
                    .build();
        }


    }

    @Override
    public ApiResponse getPetManagement(String id) {
        if (id == null || id.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Bad request")
                    .errors(true)
                    .data(null)
                    .build();
        }

        Pet pet = petRepository.findById(id).orElse(null);

        if (pet == null) {
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Pet notfound")
                    .errors(true)
                    .data(null)
                    .build();
        }

        return ApiResponse.builder()
                .status(200)
                .message("Successfully!!!")
                .errors(false)
                .data(buildPetManagementResponses(pet))
                .build();
    }

    @Override
    public ApiResponse deletePet(String id) {
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            return ApiResponse.builder()
                    .message("Can't found Pet")
                    .status(404)
                    .errors(true)
                    .data(null)
                    .build();
        }
        pet.setAdoptStatus("Unavailable");
        petRepository.save(pet);
        return ApiResponse.builder()
                .status(200)
                .message("Successfully!!!")
                .errors(false)
                .data(null)
                .build();
    }

    @Override
    public ApiResponse filterPets(Optional<String> name, Optional<String> typeName,
                                  Optional<String> colors, Optional<String> age,
                                  Optional<Boolean> gender, Optional<String> sort, Optional<Integer> page) {
        List<Pet> filterPets = petRepository.filterPets(name.orElse(null), typeName.orElse(null), colors.orElse(null),
                age.orElse(null), gender.orElse(null), sort.orElse("latest"));
        int pageSize = 9;
        int pages = page.orElse(0);
        int totalPages = (filterPets.size() + pageSize - 1) / pageSize;

        if (pages >= totalPages) {
            return ApiResponse.builder()
                    .status(HttpStatus.NO_CONTENT.value())
                    .message("No data available!!!")
                    .errors(false)
                    .data(PaginationResponse.builder().data(new ArrayList<>()).pages(0).build())
                    .build();
        }

        Pageable pageable = PageRequest.of(pages, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), filterPets.size());
        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .status(HttpStatus.NO_CONTENT.value())
                    .message("No data available!!!")
                    .errors(false)
                    .data(PaginationResponse.builder().data(Collections.emptyList()).pages(0).build())
                    .build();
        }

        List<Pet> visiblePets = filterPets.subList(startIndex, endIndex);
        Page<Pet> pagination = new PageImpl<Pet>(visiblePets, pageable, filterPets.size());
        List<PetResponse> pets;

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getHeader("Authorization");
        if (token != null) {
            User user=userService.getUserFromToken(token);
            if(user==null){
                return ApiResponse.builder()
                        .message("User not found!")
                        .data(null)
                        .errors(true)
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .build();
            }
            pets = buildPetResponse(visiblePets, user);

        }
        else {
            pets = buildPetResponse(visiblePets, null);
        }
        return ApiResponse.builder()
                .status(200)
                .message("Successfully!!!")
                .errors(false)
                .data(PaginationResponse.builder()
                        .data(pets)
                        .pages(pagination.getTotalPages())
                        .build())
                .build();
    }

    @Override
    public ApiResponse filterAdminPets(Optional<String> name, Optional<String> typeName,
                                       Optional<String> colors, Optional<String> age,
                                       Optional<Boolean> gender, Optional<String> status,
                                       Optional<LocalDate> minDate, Optional<LocalDate> maxDate,
                                       Optional<String> sort, Optional<Integer> page) {
        LocalDate minDateValue = minDate.orElse(null);
        LocalDate maxDateValue = maxDate.orElse(null);
        if (minDateValue == null && maxDateValue != null) {
            minDateValue = maxDateValue;
        }
        if (maxDateValue == null && minDateValue != null) {
            maxDateValue = minDateValue;
        }
        if (minDateValue != null && minDateValue.isAfter(maxDateValue)) {
            return ApiResponse.builder()
                    .message("The max date must be after the min date!")
                    .status(HttpStatus.CONFLICT.value())
                    .errors("The max date must be after the min date!")
                    .build();
        }
        List<Pet> filterPets = petRepository.filterAdminPets(name.orElse(null), typeName.orElse(null),
                colors.orElse(null),
                age.orElse(null), gender.orElse(null), status.orElse(null), minDateValue, maxDateValue,
                sort.orElse("latest"));

        int pageSize = 10;
        int pages = page.orElse(0);
        int totalPages = (filterPets.size() + pageSize - 1) / pageSize;

        if (pages >= totalPages) {
            return ApiResponse.builder()
                    .status(HttpStatus.NO_CONTENT.value())
                    .message("No data available!!!")
                    .errors(false)
                    .data(PaginationResponse.builder().data(new ArrayList<>()).pages(0).build())
                    .build();
        }

        Pageable pageable = PageRequest.of(pages, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), filterPets.size());
        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .status(HttpStatus.NO_CONTENT.value())  // Trả về mã trạng thái 204 (No Content)
                    .message("No data available!!!")
                    .errors(false)
                    .data(PaginationResponse.builder().data(Collections.emptyList()).pages(0).build())
                    .build();
        }


        List<Pet> visiblePets = filterPets.subList(startIndex, endIndex);
        Page<Pet> pagination = new PageImpl<Pet>(visiblePets, pageable, filterPets.size());
        List<PetDetailResponse> pets = new ArrayList<>();
        visiblePets.forEach(pet -> pets.add(buildPetResponse(pet)));

        return ApiResponse.builder()
                .status(200)
                .message("Successfully!!!")
                .errors(false)
                .data(PaginationResponse.builder().data(pets).pages(totalPages).build())
                .build();
    }

    public List<PetResponse> buildPetResponse(List<Pet> petsRaw, User user) {

        return petsRaw.stream().map(pet -> {
            boolean liked =user!=null && favouriteRepository.existByUserAndPet(user.getId(), pet.getPetId()) != null;

            Integer fosterDate= (int)ChronoUnit.DAYS.between(pet.getFosterAt(), LocalDateTime.now());

            return PetResponse.builder()
                    .id(pet.getPetId())
                    .breed(pet.getPetBreed().getBreedName())
                    .name(pet.getPetName())
                    .image(portUltils.getUrlImage(pet.getImgs()
                            .stream()
                            .findFirst()
                            .map(PetImgs::getNameImg)
                            .orElse(null)))
                    .description(pet.getDescriptions() == null ? "" : pet.getDescriptions())
                    .fosterDate(fosterDate)
                    .size(pet.getAge())
                    .sex(pet.getSex() ? "male" : "female")
                    .type(pet.getPetBreed().getPetType().getName())
                    .like(liked)
                    .adoptAt(this.getAdoptAt(pet))
                    .fostered(pet.getFosterAt())
                    .build();
        }).toList();
    }

    public PetDetailResponse buildPetResponse(Pet pet){
        return buildPetResponse(pet,null);
    }
    public PetManageResponse buildPetManagementResponses(Pet pet) {
        List<String> images = pet.getImgs().stream().map(image -> {

            return portUltils.getUrlImage(image.getNameImg());
        }).toList();

        return PetManageResponse.builder()
                .id(pet.getPetId())
                .breed(pet.getPetBreed().getBreedId())
                .name(pet.getPetName())
                .description(pet.getDescriptions() == null ? "" : pet.getDescriptions())
                .size(pet.getAge().toLowerCase().trim())
                .sex(pet.getSex() ? "male" : "female")
                .type(pet.getPetBreed().getPetType().getId())
                .fostered(pet.getFosterAt())
                .spay(pet.getIsSpay())
                .images(images)
                .color(pet.getPetColor())
                .status(pet.getAdoptStatus().toLowerCase())
                .build();

    }
    public PetResponse buildPetResponses(Pet pet, User user) {
        boolean liked = favouriteRepository.existByUserAndPet(user.getId(), pet.getPetId()) != null;

        Integer fosterDate= (int)ChronoUnit.DAYS.between(pet.getFosterAt(), LocalDateTime.now());

        return PetResponse.builder()
                .id(pet.getPetId())
                .breed(pet.getPetBreed().getBreedName())
                .name(pet.getPetName())
                .image(portUltils.getUrlImage(pet.getImgs().get(0).getNameImg()))
                .description(pet.getDescriptions() == null ? "" : pet.getDescriptions())
                .fosterDate(fosterDate)
                .size(pet.getAge())
                .sex(pet.getSex() ? "male" : "female")
                .type(pet.getPetBreed().getPetType().getName())
                .like(liked)
                .fostered(pet.getFosterAt())
                .build();
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
    public LocalDate getAdoptAt(Pet pet) {

        try {
            Adopt adopt = adoptRepository.findByPetAdopted(pet);

            return adopt.getAdoptAt();
        } catch (Exception e) {
            return null;
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
