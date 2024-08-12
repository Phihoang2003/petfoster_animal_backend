package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @Column(name = "product_id")
    @Nationalized
    private String id;

    @Column(name = "product_name")
    @Nationalized
    private String name;

    @Column(name = "product_desc")
    @Nationalized
    private String desc;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonIgnore
    @ToString.Exclude
    private ProductType productType;
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<ProductRepo> productsRepo;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Imgs> imgs;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonIgnore
    @ToString.Exclude
    private Brand brand;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<RecentView> recentViews;

}
