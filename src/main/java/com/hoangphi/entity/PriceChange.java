package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PriceChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double oldInPrice;

    private Double newInPrice;

    private Double oldOutPrice;

    private Double newOutPrice;

    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "product_repo_id")
    @JsonIgnore
    private ProductRepo productRepo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
