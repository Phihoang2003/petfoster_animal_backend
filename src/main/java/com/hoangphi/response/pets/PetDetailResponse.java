package com.hoangphi.response.pets;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetDetailResponse {
    private String id;
    private String breed;
    private String name;
    private String image;
    private String description;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fostered;
    private String size;
    private String sex;
    private String type;
    private Integer fosterDate;
    private String sterilization;
    private Boolean like;
    private List<String> images;
    private String color;
    private Boolean canAdopt;
    private String status;
}
