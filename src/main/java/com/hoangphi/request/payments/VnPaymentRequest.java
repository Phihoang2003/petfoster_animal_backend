package com.hoangphi.request.payments;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VnPaymentRequest {
    private HttpServletRequest httpServletRequest;
    private String idOrder;
    private Integer amounts;
    private String orderInfo;
}
