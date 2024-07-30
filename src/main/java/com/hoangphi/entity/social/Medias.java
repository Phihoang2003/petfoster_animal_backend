package com.hoangphi.entity.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class Medias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Boolean isVideo;
    @Column(name = "[index]")
    private Integer index;
    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Posts post;
}
