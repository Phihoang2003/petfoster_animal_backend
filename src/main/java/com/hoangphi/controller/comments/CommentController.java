package com.hoangphi.controller.comments;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.comments.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/like/{comment_id}")
    public ResponseEntity<ApiResponse> likedComment(@PathVariable("comment_id") Integer commentId, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(commentService.likeComment(commentId, token));
    }
}
