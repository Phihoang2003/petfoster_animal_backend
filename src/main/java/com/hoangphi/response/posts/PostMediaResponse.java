package com.hoangphi.response.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostMediaResponse {
    private Integer id;
    private String url;
    private Boolean isVideo;
    private Integer index;
}
