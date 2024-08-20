package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ShippingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;
    @Nationalized
    private String address;
    @Nationalized
    private String province;
    @Nationalized
    private String district;
    @Nationalized
    private String ward;

    private String phone;

    private Integer shipFee;

    @OneToMany(mappedBy = "shippingInfo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Orders> orders;

    @ManyToOne
    @JoinColumn(name = "delivery_company_id")
    @JsonIgnore
    private DeliveryCompany deliveryCompany;
}
