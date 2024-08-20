package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @CreationTimestamp
    private LocalDate createAt;

    @ManyToOne
    @JoinColumn(name = "shipping_info_id")
    @JsonIgnore
    private ShippingInfo shippingInfo;
    @Nationalized
    private String descriptions;

    private Double total;
    @Nationalized
    private String status;

    private String ghnCode;

    private String expectedDeliveryTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    @JsonIgnore
    private Payment payment;

    @Column(name = "[read]")
    private Boolean read;

    @Column(name = "[print]")
    private Integer print;
}
