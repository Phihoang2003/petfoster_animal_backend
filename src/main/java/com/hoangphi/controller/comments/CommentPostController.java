package com.hoangphi.controller.comments;

import com.hoangphi.request.comments.CommentPostRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.comments.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/comments")
public class CommentPostController {
    private final CommentService commentService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> commentPosts(@RequestBody CommentPostRequest commentPostRequest,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(commentService.commentPost(commentPostRequest, token));
    }

}
