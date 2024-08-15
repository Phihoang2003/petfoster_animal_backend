package com.hoangphi.request.pets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetRequest {
    @NotBlank(message = "Pet Name can't be blank!")
    @NotEmpty(message = "Pet Name can't be empty!")
    private String name;

    @NotBlank(message = "Pet Color can't be blank!")
    @NotEmpty(message = "Pet Color can't be empty!")
    private String color;

    private Boolean isSpay;

    private LocalDate fosterAt;

    private String description;
    @NotBlank(message = "Pet Size can't be blank!")
    @NotEmpty(message = "Pet Size can't be empty!")
    private String size;

    private Boolean sex;
    @NotBlank(message = "Pet Breed ID can't be blank!")
    @NotEmpty(message = "Pet Breed can't be empty!")
    private String breed;
    private String status;
}
