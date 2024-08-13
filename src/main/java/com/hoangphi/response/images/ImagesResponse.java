package com.hoangphi.response.images;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImagesResponse {
    private Integer id;
    private String name;
    private String image;
}
