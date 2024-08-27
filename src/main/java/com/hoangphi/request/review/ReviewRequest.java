package com.hoangphi.request.review;

import com.hoangphi.constant.RespMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {
    @NotNull(message = RespMessage.NOT_EMPTY)
    private Integer orderId;
    @NotNull(message = RespMessage.NOT_EMPTY)
    private String productId;
    @NotNull(message = RespMessage.NOT_EMPTY)
    @NotBlank(message = "Comment can't be blank")
    private String comment;
    @NotNull(message = RespMessage.NOT_EMPTY)
    @Min(value = 1, message = "Min is 1")
    @Max(value = 5, message = "Max is 5")
    private Integer rate;
}
