package com.hoangphi.response.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionReportResponse {
    private Double day;
    private Double month;
    private Double year;
}
