package com.hoangphi.service.comments;

import com.hoangphi.request.comments.CommentPostRequest;
import com.hoangphi.response.ApiResponse;

public interface CommentService {
    ApiResponse commentPost(CommentPostRequest commentPostRequest, String token);
    ApiResponse likeComment(Integer commentId, String token);

}
