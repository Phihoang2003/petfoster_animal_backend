package com.hoangphi.service.impl.adopt;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.constant.AdoptStatus;
import com.hoangphi.constant.PetStatus;
import com.hoangphi.entity.Addresses;
import com.hoangphi.entity.Adopt;
import com.hoangphi.entity.Pet;
import com.hoangphi.entity.User;
import com.hoangphi.repository.AddressRepository;
import com.hoangphi.repository.AdoptRepository;
import com.hoangphi.repository.PetRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.adopts.AdoptsRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.adopts.AdoptsResponse;
import com.hoangphi.service.adopt.AdoptService;
import com.hoangphi.service.impl.pets.PetServiceImpl;
import com.hoangphi.service.impl.users.UserServiceImpl;
import com.hoangphi.utils.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdoptServiceImpl implements AdoptService {
    private final AdoptRepository adoptRepository;
    private final PetRepository petRepository;
    private final AddressRepository addressRepository;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final FormatUtils formatUtils;
    private final UserServiceImpl userServiceImpl;
    private final PetServiceImpl petServiceImpl;


    @Override
    public ApiResponse adopt(String jwt, AdoptsRequest adoptsRequest) {
        String username = jwtProvider.getUsernameFromToken(jwt);
        if (username == null || username.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("User not found!!!")
                    .errors(true)
                    .build();
        }

        User curUser = userRepository.findByUsername(username).orElse(null);
        if (curUser == null) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("User not found!!!")
                    .errors(true)
                    .build();
        }
        User user=userRepository.findById(adoptsRequest.getUserId()).orElse(null);
        if(user==null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("User not found!!!")
                    .errors(true)
                    .build();
        }
        if (user != curUser) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("You don't have permission to adopt for this user!!!")
                    .errors(true)
                    .build();
        }
        Addresses address = addressRepository.findByIdAndUser(adoptsRequest.getAddressId(), user.getUsername());
        if (address == null) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Address not found by this user!!!")
                    .errors(true)
                    .build();
        }
        Pet pet=petRepository.findById(adoptsRequest.getPetId()).orElse(null);
        if(pet==null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Pet not found!!!")
                    .errors(true)
                    .build();
        }
        if(pet.getAdoptStatus().equalsIgnoreCase("adopted")){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Pet has been adopted!!!")
                    .errors(true)
                    .build();
        }
        if(pet.getAdoptStatus().equalsIgnoreCase(PetStatus.ADOPTED.getValue())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("This pet is currently adopted, so we are unable to proceed with adoption, please comeback when the pet is available!!!")
                    .errors(true)
                    .build();
        }
        if(pet.getAdoptStatus().equalsIgnoreCase(PetStatus.FOSTERED.getValue())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("This pet is currently fostered, so we are unable to proceed with adoption, please comeback when the pet is available!!!")
                    .errors(true)
                    .build();
        }
        if(adoptRepository.exitsAdopted(adoptsRequest.getPetId())!=null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("This pet was adopted or registered")
                    .errors(true)
                    .build();
        }
        if (adoptRepository.existsByPetAndUser(adoptsRequest.getPetId(), adoptsRequest.getUserId()) != null) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("The adoption was accepted!!!")
                    .errors(true)
                    .build();
        }
        Adopt adopt=Adopt.builder()
                .user(user)
                .pet(pet)
                .address(formatUtils.getAddress(
                        address.getAddress(),
                        address.getWard(),
                        address.getDistrict(),
                        address.getProvince()))
                .phone(address.getPhone())
                .registerAt(LocalDate.now())
                .status(AdoptStatus.WAITING.getValue())
                .build();
        adoptRepository.save(adopt);
        AdoptsResponse adoptsResponse=buildAdoptsResponse(adopt);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Successfully!!!")
                .errors(false)
                .data(adoptsResponse)
                .build();

    }
    public AdoptsResponse buildAdoptsResponse(Adopt adopt) {
        return AdoptsResponse.builder()
                .id(adopt.getAdoptId())
                .state(adopt.getStatus())
                .user(userServiceImpl.buildUserProfileResponse(adopt.getUser()))
                .pet(petServiceImpl.buildPetResponses(adopt.getPet(), adopt.getUser()))
                .adoptAt(adopt.getAdoptAt())
                .registerAt(adopt.getRegisterAt())
                .cancelReason(adopt.getCancelReason() != null ? adopt.getCancelReason() : "")
                .phone(adopt.getPhone())
                .address(adopt.getAddress())
                .pickUpDate(adopt.getPickUpAt())
                .build();
    }
}
