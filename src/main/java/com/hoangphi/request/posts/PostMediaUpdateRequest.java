package com.hoangphi.request.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMediaUpdateRequest {
    private Integer id;
    private Integer index;
    private MultipartFile file;
}
