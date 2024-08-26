package com.hoangphi.request.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoMoPaymentRequest {
    private String orderId;
    private String orderInfo;
    private Integer amount;

}
