package com.hoangphi.request.variants;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRepoRequest {
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Integer size;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Double inPrice;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Double outPrice;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Integer quantity;
}
