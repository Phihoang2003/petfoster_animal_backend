package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ProductRepo {
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer size;
    private Double inPrice;
    private Double outPrice;
    private Integer quantity;
    private Boolean inStock;
    @JsonIgnore
    private Boolean isActive;
}
