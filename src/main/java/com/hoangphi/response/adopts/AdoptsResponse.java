package com.hoangphi.response.adopts;

import com.hoangphi.response.pets.PetDetailResponse;
import com.hoangphi.response.pets.PetResponse;
import com.hoangphi.response.users.UserProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdoptsResponse {
    private Integer id;
    private String state;
    private UserProfileResponse user;
    private PetResponse pet;
    private LocalDate registerAt;
    private LocalDate adoptAt; // can null if watting or cancel
    private String cancelReason;
    private String phone;
    private String address;
    private LocalDate pickUpDate;
}
