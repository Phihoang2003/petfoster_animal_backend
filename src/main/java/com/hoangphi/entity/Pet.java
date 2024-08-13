package com.hoangphi.entity;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pet {
    @Id
    private String petId;
    private String petName;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    @JsonIgnore
    private PetBreed petBreed;

    private Boolean sex;

    @Nationalized
    private String petColor;

    @Nationalized
    private String age;

    private Boolean isSpay;

    @CreationTimestamp
    private LocalDateTime createAt;

    private LocalDateTime fosterAt;

    @Nationalized
    private String descriptions;
    @Nationalized
    private String adoptStatus;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<PetImgs> imgs;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Favorite> favorites;


}
