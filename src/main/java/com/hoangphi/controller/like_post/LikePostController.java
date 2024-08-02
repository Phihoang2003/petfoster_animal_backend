package com.hoangphi.controller.like_post;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/like-posts")
public class LikePostController {
    private final PostService postService;
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> likePost(@PathVariable("id") String uuid,
                                                @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(postService.likePost(uuid, token));
    }
}
