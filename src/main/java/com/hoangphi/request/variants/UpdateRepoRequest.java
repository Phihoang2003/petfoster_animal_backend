package com.hoangphi.request.variants;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRepoRequest {

    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Double inPrice;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Double outPrice;
    @NotBlank(message = RespMessage.NOT_EMPTY)
    private Integer quantity;

}
