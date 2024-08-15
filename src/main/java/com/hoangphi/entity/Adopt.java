package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Adopt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adoptId;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToOne
    @JoinColumn(name = "pet_id")
    @ToString.Exclude
    private Pet pet;

    private LocalDate adoptAt;
    private String status;
}
