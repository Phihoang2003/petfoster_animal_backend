package com.hoangphi.service.posts;

import com.hoangphi.request.posts.PostRequest;
import com.hoangphi.request.posts.PostUpdateRequest;
import com.hoangphi.response.ApiResponse;

public interface PostService {
    ApiResponse create(PostRequest data, String token);
    ApiResponse update(PostUpdateRequest data, String id, String token);
}
