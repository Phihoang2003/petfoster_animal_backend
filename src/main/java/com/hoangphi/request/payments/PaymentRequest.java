package com.hoangphi.request.payments;

import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer orderId;
    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer amount;
    @NotNull(message = RespMessage.NOT_EMPTY)
    private Boolean isPaid;
    @NotNull(message = RespMessage.NOT_EMPTY)
    private String payAt;
    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer transactionNumber;
    private PaymentMethod paymentMethod;
}
