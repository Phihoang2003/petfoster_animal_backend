package com.hoangphi.response.pets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PetAttributesResponse {
    List<PetAttributeResponse> colors;
    List<PetAttributeResponse> states;
    List<PetAttributeResponse> breeds;
    List<PetAttributeResponse> typies;
}
