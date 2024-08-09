package com.hoangphi.request.brands;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandRequest {
    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer id;
    @NotNull(message = RespMessage.NOT_EMPTY)
    @NotBlank(message = "Name can't be blank")
    private String name;
}
