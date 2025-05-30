package com.hoangphi.service.comments;

import com.hoangphi.request.comments.CommentPostRequest;
import com.hoangphi.response.ApiResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentService {
    ApiResponse commentPost(CommentPostRequest commentPostRequest, String token);
    ApiResponse likeComment(Integer commentId, String token);

    ApiResponse getCommentWithIdPost(String uuid, Optional<Integer> page);

    ApiResponse deleteComment(Integer id,String token);

}
