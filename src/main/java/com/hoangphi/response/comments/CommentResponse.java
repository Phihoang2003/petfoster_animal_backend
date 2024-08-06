package com.hoangphi.response.comments;

import com.hoangphi.response.users.UserProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Integer id;
    private UserProfileResponse user;
    private String comment;
    private Integer likes;
    private Boolean isLike;
    private Date createAt;
    private List<CommentResponse> children;
    private Boolean owner;



}
