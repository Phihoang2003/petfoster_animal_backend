package com.hoangphi.response.price_changes;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceChangeResponse {
    private Integer id;
    private Double newInPrice;
    private Double newOutPrice;
    private Double oldInPrice;
    private Double oldOutPrice;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updateAt;
    private Integer size;
    private Object user;
}
