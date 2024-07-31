package com.hoangphi.service.posts;

import com.hoangphi.request.posts.PostRequest;
import com.hoangphi.response.ApiResponse;

public interface PostService {
    ApiResponse create(PostRequest data, String token);
}
