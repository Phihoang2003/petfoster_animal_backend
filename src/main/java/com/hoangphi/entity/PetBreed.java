package com.hoangphi.entity;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetBreed {
    @Id
    private String breedId;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonIgnore
    @ToString.Exclude
    private PetType petType;
    @Nationalized
    private String breedName;

    @OneToMany(mappedBy = "petBreed", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Pet> pets=new ArrayList<>();
}
