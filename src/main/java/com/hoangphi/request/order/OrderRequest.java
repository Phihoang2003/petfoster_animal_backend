package com.hoangphi.request.order;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer addressId;

    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer deliveryId;

    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer methodId;

    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer ship;

    @Valid
    @NotEmpty(message = RespMessage.NOT_EMPTY)
    private List<OrderItem> orderItems;

}
