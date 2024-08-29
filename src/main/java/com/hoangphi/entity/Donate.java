package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Donate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate donateAt;
    @Nationalized
    private String donater;
    @Nationalized
    private String descriptions;

    private Double donateAmount;

    @Nationalized
    private String beneficiaryBank;

    private String toAccountNumber;

    private Integer idTransaction;
}
