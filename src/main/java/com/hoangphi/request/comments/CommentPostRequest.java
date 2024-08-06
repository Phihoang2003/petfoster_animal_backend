package com.hoangphi.request.comments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentPostRequest {
    private String uuid;
    private String comment;
    private Integer replyId;
}
