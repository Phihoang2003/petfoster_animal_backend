package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Addresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String recipient;
    private String phone;
    @Nationalized
    private String province;
    @Nationalized
    private String district;
    @Nationalized
    private String ward;
    @Nationalized
    private String address;
    @Setter
    @JoinColumn(name = "is_default")
    private Boolean isDefault;
    @JoinColumn(name = "create_at")
    @CreationTimestamp
    private Date createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

}
