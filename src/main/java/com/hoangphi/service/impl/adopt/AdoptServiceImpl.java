package com.hoangphi.service.impl.adopt;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.constant.AdoptStatus;
import com.hoangphi.constant.Constant;
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
import com.hoangphi.request.adopts.CancelAdoptRequest;
import com.hoangphi.request.adopts.UpdatePickUpDateRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.adopts.AdoptsResponse;
import com.hoangphi.response.common.PaginationResponse;
import com.hoangphi.service.adopt.AdoptService;
import com.hoangphi.service.impl.pets.PetServiceImpl;
import com.hoangphi.service.impl.users.UserServiceImpl;
import com.hoangphi.utils.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public ApiResponse doneAdoption(Integer id) {
        if(id==null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Adopt not found!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        Adopt adopt=adoptRepository.findById(id).orElse(null);
        if(adopt==null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Adopt not found!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        //was adopted
        if(adopt.getStatus().equalsIgnoreCase(AdoptStatus.ADOPTED.getValue())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Adopt has been done!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        //has register yet
        if(adopt.getStatus().equalsIgnoreCase(AdoptStatus.WAITING.getValue())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("The adoption hasn't accepted yet!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        //was canceled
        if(adopt.getStatus().equalsIgnoreCase(AdoptStatus.CANCELLED_BY_CUSTOMER.getValue())||
            adopt.getStatus().equalsIgnoreCase(AdoptStatus.CANCELLED_BY_ADMIN.getValue())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("The adoption was canceled!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        adopt.setStatus(AdoptStatus.ADOPTED.getValue());
        adopt.setAdoptAt(LocalDate.now());
        adoptRepository.save(adopt);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully!!!")
                .data(buildAdoptsResponse(adopt))
                .errors(false)
                .build();

    }

    @Override
    public ApiResponse acceptAdoption(Integer id, UpdatePickUpDateRequest updatePickUpDateRequest) {
        Adopt adopt=adoptRepository.findById(id).orElse(null);
        if(adopt==null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Adopt not found!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        if(adopt.getStatus().equalsIgnoreCase(AdoptStatus.ADOPTED.getValue())||
            adopt.getStatus().equalsIgnoreCase(AdoptStatus.REGISTERED.getValue())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("This adoption was accepted!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        if(updatePickUpDateRequest.getPickUpDate().isBefore(LocalDate.now())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Pick up date must be greater than or equal to the current date!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        adopt.setStatus(AdoptStatus.REGISTERED.getValue());
        adopt.setPickUpAt(updatePickUpDateRequest.getPickUpDate());
        adoptRepository.save(adopt);

        List<Adopt> rejectAdopts=adoptRepository.findByUserIgnoreUserId(adopt.getUser().getId(),
                adopt.getPet().getPetId());

        rejectAdopts.forEach(item -> {
            item.setStatus(AdoptStatus.CANCELLED_BY_ADMIN.getValue());
            item.setCancelReason("Thank you for your interest in " + adopt.getPet().getPetName()
                    + ". We are very sorry that " + adopt.getPet().getPetName()
                    + " has been adopted by someone else.");
        });

        adoptRepository.saveAll(rejectAdopts);

        List<AdoptsResponse> reuslt = new ArrayList<>();

        if (!rejectAdopts.isEmpty()) {
            reuslt = rejectAdopts.stream().map(this::buildAdoptsResponse).toList();
        }

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully!!!")
                .errors(false)
                .data(reuslt)
                .build();

    }

    @Override
    public ApiResponse getAdopts(String jwt, Optional<Integer> page, Optional<String> status) {
        String username = jwtProvider.getUsernameFromToken(jwt);
        if (username == null || username.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("User not found!!!")
                    .errors(true)
                    .build();
        }
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("User not found!!!")
                    .errors(true)
                    .build();
        }
        String unWrapStatus= status.orElse("all");
        List<Adopt> adopts;
        if(!unWrapStatus.equalsIgnoreCase("all")){
            adopts=adoptRepository.findByUser(user.getId(),unWrapStatus);
        }else{
            adopts=adoptRepository.findByUser(user.getId());
        }
        if (adopts.isEmpty()) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                    .message("No data available!!!").errors(false)
                    .data(PaginationResponse.builder().data(new ArrayList<>()).pages(0).build())
                    .build();
        }
        int pageSize = 10;
        int pages = page.orElse(0);
        int totalPages = (adopts.size() + pageSize - 1) / pageSize;

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
        int endIndex = Math.min(startIndex + pageable.getPageSize(), adopts.size());
        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .status(HttpStatus.NO_CONTENT.value())
                    .message("No data available!!!")
                    .errors(true)
                    .data(PaginationResponse.builder().data(adopts).pages(0).build())
                    .build();
        }

        List<Adopt> visibleAdopts = adopts.subList(startIndex, endIndex);

        Page<Adopt> pagination = new PageImpl<Adopt>(visibleAdopts, pageable, adopts.size());

        // get adopts response & sort
        List<AdoptsResponse> adoptsResponse = visibleAdopts.stream()
                .map(this::buildAdoptsResponse)
                .collect(Collectors.toList());

        adoptsResponse.sort(Comparator
                .comparing((AdoptsResponse adoptResponse) -> adoptResponse.getRegisterAt() != null
                        ? adoptResponse.getRegisterAt()
                        : Constant.MIN_DATE)
                .reversed());

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully!!!")
                .errors(false)
                .data(PaginationResponse.builder().data(adoptsResponse)
                        .pages(pagination.getTotalPages()).build())
                .build();

    }

    @Override
    public ApiResponse getAdoptOtherUser(Integer adoptId) {
        if (adoptId == null) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                    .message("Adopt not found!!!").errors(true).build();
        }

        Adopt adopt = adoptRepository.findById(adoptId).orElse(null);

        if (adopt == null) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                    .message("Adopt not found!!!").errors(true).build();
        }


        List<Adopt> adopts = adoptRepository.findByUserIgnoreUserId(adopt.getUser().getId(),
                adopt.getPet().getPetId());

        if (adopts == null || adopts.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("No data available!!!")
                    .errors(true)
                    .data(new ArrayList<>())
                    .build();
        }

        List<AdoptsResponse> reuslt = adopts.stream().map(this::buildAdoptsResponse).toList();

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully!!!")
                .errors(false)
                .data(reuslt)
                .build();
    }

    @Override
    public ApiResponse cancelAdopt(Integer id, CancelAdoptRequest cancelAdoptRequest) {
        Adopt adopt=adoptRepository.findById(id).orElse(null);
        if(adopt==null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Adopt not found!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        if(adopt.getStatus().equalsIgnoreCase(AdoptStatus.ADOPTED.getValue())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("This adoption was accepted.Cannot cancel!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        if(adopt.getStatus().equalsIgnoreCase(AdoptStatus.CANCELLED_BY_CUSTOMER.getValue())||
            adopt.getStatus().equalsIgnoreCase(AdoptStatus.CANCELLED_BY_ADMIN.getValue())){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("This adoption was canceled.Can't cancel again!!!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        adopt.setStatus(AdoptStatus.CANCELLED_BY_ADMIN.getValue());
        adopt.setCancelReason(cancelAdoptRequest.getCancelReason());
        adoptRepository.save(adopt);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully!!!")
                .data(buildAdoptsResponse(adopt))
                .errors(false)
                .build();
    }

    @Override
    public ApiResponse cancelAdoptByUser(Integer id, String jwt, CancelAdoptRequest cancelAdoptRequest) {
        Adopt adopt = adoptRepository.findById(id).orElse(null);
        if (adopt == null) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                    .message("Adopt not found!!!").errors(true).build();
        }

        String username = jwtProvider.getUsernameFromToken(jwt);
        if (username == null || username.isEmpty()) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                    .message("Username is not exists!!!").errors(true).build();
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value()).message("User not found!!!")
                    .errors(true).build();
        }

        if (adoptRepository.existsByUser(user.getId(), id) == null) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                    .message("You don't have permission to cancel!!!").errors(true).build();
        }

        // check adoption status was adopted or not
        if (adopt.getStatus().equalsIgnoreCase(AdoptStatus.ADOPTED.getValue())) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                    .message("The pet was adopted. Cannot cancel!!!").errors(true).build();
        }

        // check adoption status was cancel or not
        if (adopt.getStatus().equalsIgnoreCase(AdoptStatus.CANCELLED_BY_ADMIN.getValue())
                || adopt.getStatus().equalsIgnoreCase(AdoptStatus.CANCELLED_BY_CUSTOMER.getValue())) {
            return ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                    .message("The adoption was cancel. Cannot cancel again!!!").errors(true)
                    .build();
        }

        // reject the adoption
        adopt.setStatus(AdoptStatus.CANCELLED_BY_CUSTOMER.getValue());
        adopt.setCancelReason(cancelAdoptRequest.getCancelReason());
        adoptRepository.save(adopt);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully!!!")
                .errors(false)
                .data(this.buildAdoptsResponse(adopt))
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
