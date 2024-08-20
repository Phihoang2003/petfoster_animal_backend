package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nationalized
    private String method;
    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;
}
