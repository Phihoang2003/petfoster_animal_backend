package com.hoangphi.controller.posts;

import com.hoangphi.request.posts.PostRequest;
import com.hoangphi.request.posts.PostUpdateRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/posts")
public class PostUserController {
    private final PostService postService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> createPost(@ModelAttribute PostRequest data,
                                                  @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(postService.create(data, token));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePost(@ModelAttribute PostUpdateRequest data, @PathVariable("id") String id,
                                                  @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(postService.update(data, id, token));
    }

}
