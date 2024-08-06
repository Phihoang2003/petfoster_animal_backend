package com.hoangphi.response.posts;

import com.hoangphi.response.users.UserProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponse {
    private String id;
    private String title;
    private Boolean isLike;
    private Integer likes;
    private Integer comments;
    private UserProfileResponse user;
    private List<PostMediaResponse> images;
    private Date createdAt;
    private Boolean owner;
    private Boolean edit;


}
