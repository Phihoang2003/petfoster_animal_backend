package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
}
