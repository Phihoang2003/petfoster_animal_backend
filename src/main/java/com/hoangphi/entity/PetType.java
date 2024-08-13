package com.hoangphi.entity;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PetType {
    @Id
    @Column(name="type_id")
    private String id;

    @Column(name="type_name")
    private String name;

    @OneToMany(mappedBy = "petType", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<PetBreed> breeds = new ArrayList<>();


}
