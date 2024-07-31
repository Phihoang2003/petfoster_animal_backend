package com.hoangphi.service.impl.posts;

import com.hoangphi.constant.Constant;
import com.hoangphi.entity.User;
import com.hoangphi.entity.social.Medias;
import com.hoangphi.entity.social.Posts;
import com.hoangphi.repository.PostRepository;
import com.hoangphi.request.posts.PostMediaRequest;
import com.hoangphi.request.posts.PostRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.posts.PostDetailResponse;
import com.hoangphi.service.impl.users.UserServiceImpl;
import com.hoangphi.service.posts.PostService;
import com.hoangphi.service.user.UserService;
import com.hoangphi.utils.ImageUtils;
import com.hoangphi.utils.parent.OptionCreateAndSaveFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserService userService;
    private final PostRepository postsRepository;

    @Override
    public ApiResponse create(PostRequest data, String token) {
        User user=userService.getUserFromToken(token);
        if(user==null){
            return ApiResponse.builder()
                    .message("Unauthorized")
                    .status(HttpStatus.FORBIDDEN.value())
                    .errors(true)
                    .data(null)
                    .build();

        }
        if (validateCreatePost(data)) {
            return ApiResponse.builder()
                    .message("Create failure Make sure you have submitted the correct data")
                    .errors(false)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .build();
        }
        Posts posts = buildPostFromPostRequest(data, user);

        if (posts.getMedias().isEmpty()) {
            return ApiResponse.builder()
                    .message("Create failure Make sure you have submitted the correct data")
                    .errors(false)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .build();
        }

        postsRepository.save(posts);

        return ApiResponse.builder()
                .message("Successfuly")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(posts)
                .build();

    }
    public PostDetailResponse buildDetailResponse(Posts posts){
        return null;
    }
    public Posts buildPostFromPostRequest(PostRequest data, User user){
        UUID uuid=UUID.randomUUID();
        Posts posts=Posts.builder()
                .uuid(uuid.toString())
                .title(data.getTitle())
                .user(user)
                .build();
        List<Medias> medias=data.getMedias().stream().map(item->{
            Boolean isVideo=item.getFile().getContentType().equals("video/mp4");
            File file= ImageUtils.createFileAndSave("medias\\",item.getFile(), OptionCreateAndSaveFile.builder()
                    .acceptExtentions(Constant.ACCEPT_EXTENTION)
                    .build());
            System.out.println("file name: "+file.getName());
            return Medias.builder()
                    .index(item.getIndex())
                    .isVideo(isVideo)
                    .name(file.getName())
                    .post(posts)
                    .build();

        }).toList();
        posts.setMedias(medias);

        return posts;
    }

    public Boolean validateCreatePost(PostRequest data){
        if(data.getTitle()==null||data.getTitle().isEmpty()){
            return true;
        }
        if(data.getMedias().size()<=0){
            return true;
        }
        List<PostMediaRequest> medias = data.getMedias();
        if (medias.size() > 1) {
            for (PostMediaRequest media : medias) {
                if (media.getFile().getContentType().equals("video/mp4")) {
                    return true;
                }
            }
        }
        return false;
    }
}
