package com.hoangphi.service.impl.pages;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.entity.Pet;
import com.hoangphi.entity.User;
import com.hoangphi.entity.social.Posts;
import com.hoangphi.repository.*;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.pages.HomePageResponse;
import com.hoangphi.response.pages.ImpactOfYearResponse;
import com.hoangphi.response.pets.PetResponse;
import com.hoangphi.response.posts.PostReponse;
import com.hoangphi.service.pages.HomePageService;
import com.hoangphi.service.pets.PetService;
import com.hoangphi.service.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomePageServiceImpl implements HomePageService {
    private final PetRepository petRepository;
    private final PetService petService;
    private final PostRepository postsRepository;
    private final PostService postService;
    private final DonateRepository donateRepository;
    private final AdoptRepository adoptRepository;
    private final SecurityUtils jwtProvider;
    private final UserRepository userRepository;
    @Override
    public ApiResponse homepage() {

        // get pets raw data
        List<Pet> petsRaw = petRepository.findAllByActive();

        // get posts raw data
        List<Posts> postsRaw = postsRepository.findAllByActive();

        List<PostReponse> posts = postService.buildPostHomePageResponses(postsRaw);

        // get impact
        List<ImpactOfYearResponse> impacts = Arrays.asList(
                new ImpactOfYearResponse("dog.svg", petRepository.findAll().size() + "",
                        "Total pets fostered", null),
                new ImpactOfYearResponse("cats.svg",
                        donateRepository.getDonation() == null ? "0"
                                : donateRepository.getDonation() + "",
                        "In products & donations", "$"),
                new ImpactOfYearResponse("home-dog.svg", adoptRepository.getAdoptedPets().size() + "",
                        "Total pets have a home", null));

        // get username from token requested to user
        String username = jwtProvider.getCurrentUsername();

        if (username != null) {
            // check username
            if ( username.isEmpty()) {
                return ApiResponse.builder().message("Failure").status(HttpStatus.BAD_REQUEST.value())
                        .errors(true)
                        .data(new ArrayList<>()).build();
            }
            // get user to username
            User user = userRepository.findByUsername(username).orElse(null);

            // check user
            if (user == null) {
                return ApiResponse.builder().message("Failure").status(HttpStatus.BAD_REQUEST.value())
                        .errors(true)
                        .data(new ArrayList<>()).build();
            }

            // get pets
            List<PetResponse> pets = petService.buildPetResponse(petsRaw, user);

            return ApiResponse.builder().message("Successfully").status(200).errors(false)
                    .data(HomePageResponse.builder().impactOfYear(impacts).pets(pets)
                            .postsPreview(posts).build())
                    .build();
        }

        List<PetResponse> pets = petService.buildPetResponse(petsRaw,null);

        return ApiResponse.builder().message("Successfuly").status(200).errors(false)
                .data(HomePageResponse.builder().impactOfYear(impacts).pets(pets).postsPreview(posts)
                        .build())
                .build();
    }
}
