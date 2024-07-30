package com.hoangphi.response.posts;

import com.hoangphi.response.users.UserProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostReponse {
    private String id;
    private String title;
    //hình ảnh để hiển thị video(hình ảnh thu nhỏ)
    private String thumbnail;
    private Boolean containVideo;
    private Boolean isLike;
    private Integer likes;
    private Integer comments;
    private UserProfileResponse user;
}
