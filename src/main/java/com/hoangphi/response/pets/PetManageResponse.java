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
public class PetManageResponse {
    private String id;
    private String breed;
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fostered;
    private String size;
    private String sex;
    private String type;
    private Boolean spay;
    private List<String> images;
    private String color;
    private String status;
}
