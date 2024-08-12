package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ProductRepo {
    @Id
    @Column(name = "product_repo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

    private Integer size;
    private Double inPrice;
    private Double outPrice;
    private Integer quantity;
    private Boolean inStock;
    @JsonIgnore
    private Boolean isActive;
}
