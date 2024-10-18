package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Nationalized
    private String transactionNumber;

    private Double amount;

    private Boolean isPaid;

    private LocalDateTime payAt;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Orders> orders;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    @JsonIgnore
    @ToString.Exclude
    private PaymentMethod paymentMethod;
}
