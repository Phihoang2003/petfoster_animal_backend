package com.hoangphi.request.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingProductRequest {
    @Builder.Default
    private String name="";

    @Builder.Default
    private String code="";

    @Builder.Default
    private Integer quantity=1;

    @Builder.Default
    private Integer price=0;

    @Builder.Default
    private Integer weight = 1200;
}
