package com.hoangphi.controller.posts;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("{username}")
    public ResponseEntity<ApiResponse> postOfUser(@PathVariable("username") String username,
                                                  @RequestParam("type") Optional<String> type,
                                                  @RequestParam("page") Optional<Integer> page){
        return ResponseEntity.ok(postService.postsOfUser(username, page, type));
    }
    @GetMapping("")
    public ResponseEntity<ApiResponse> posts(@RequestParam("search") Optional<String> search,
                                             @RequestParam("page") Optional<Integer> page) {
        return ResponseEntity.ok(postService.posts(search, page));
    }
}
