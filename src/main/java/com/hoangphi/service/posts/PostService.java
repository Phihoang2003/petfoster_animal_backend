package com.hoangphi.service.posts;

import com.hoangphi.entity.social.Medias;
import com.hoangphi.entity.social.Posts;
import com.hoangphi.request.posts.PostRequest;
import com.hoangphi.request.posts.PostUpdateRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.posts.PostMediaResponse;
import com.hoangphi.response.posts.PostReponse;

import java.util.List;
import java.util.Optional;

public interface PostService {
    ApiResponse create(PostRequest data, String token);
    ApiResponse update(PostUpdateRequest data, String id, String token);
    ApiResponse delete(String uuid, String token);
    ApiResponse likePost(String uuid, String token);
    ApiResponse highlight();
    ApiResponse highlightOfUser(String username);
    ApiResponse detailPost(String uuid);
    List<PostReponse> buildPostHomePageResponses(List<Posts> posts);

    ApiResponse posts(String search, Optional<Integer> page);

    ApiResponse postsOfUser(String username, Optional<Integer> page, Optional<String> type);

    PostMediaResponse builPostMediaResponse(Medias media);

}
