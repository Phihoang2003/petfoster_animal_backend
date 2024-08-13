package com.hoangphi.response.pets;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    private String id;
    private String breed;
    private String name;
    private String image;
    private String description;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fostered;
    private String size;
    private String sex;
    private String type;
    //Date fostered <=> fosterAt
    private Integer fosterDate;
    private Boolean like;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date adoptAt;
}
