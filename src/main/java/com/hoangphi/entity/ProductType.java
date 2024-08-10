package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ProductType {
    @Id
    @Column(name = "product_type_id")
    private String id;
    @Column(name = "product_type_name")
    private String name;

    @OneToMany(mappedBy = "productType",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products=new ArrayList<>();
}
