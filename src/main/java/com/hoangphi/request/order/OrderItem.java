package com.hoangphi.request.order;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private String productId;

    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer size;

    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer quantity;
}
