package com.hoangphi.response.pages;

import com.hoangphi.entity.Pet;
import com.hoangphi.response.pets.PetDetailResponse;
import com.hoangphi.response.pets.PetResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PetDetailPageResponse {
    private PetDetailResponse pet;
    private List<PetResponse> others;
}
