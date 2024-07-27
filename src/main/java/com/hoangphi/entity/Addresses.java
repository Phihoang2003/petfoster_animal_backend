package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
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
    @JoinColumn(name = "is_default")
    private Boolean isDefault;
    @JoinColumn(name = "create_at")
    private Date createAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

}
