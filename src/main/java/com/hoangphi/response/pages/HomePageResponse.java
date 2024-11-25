package com.hoangphi.response.pages;

import com.hoangphi.response.pets.PetResponse;
import com.hoangphi.response.posts.PostReponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HomePageResponse {
    private List<ImpactOfYearResponse> impactOfYear;
    private List<PetResponse> pets;
    private List<PostReponse> postsPreview;
}
