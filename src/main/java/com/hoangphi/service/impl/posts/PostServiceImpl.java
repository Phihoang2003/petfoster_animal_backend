package com.hoangphi.service.impl.posts;

import com.hoangphi.constant.Constant;
import com.hoangphi.entity.User;
import com.hoangphi.entity.social.Likes;
import com.hoangphi.entity.social.Medias;
import com.hoangphi.entity.social.Posts;
import com.hoangphi.repository.LikeRepository;
import com.hoangphi.repository.MediasRepository;
import com.hoangphi.repository.PostRepository;
import com.hoangphi.request.posts.PostMediaRequest;
import com.hoangphi.request.posts.PostMediaUpdateRequest;
import com.hoangphi.request.posts.PostRequest;
import com.hoangphi.request.posts.PostUpdateRequest;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserService userService;
    private final PostRepository postsRepository;
    private final MediasRepository mediasRepository;
    private final LikeRepository likeRepository;

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

    @Override
    public ApiResponse update(PostUpdateRequest data, String id, String token) {
        User user = userService.getUserFromToken(token);

        if (user == null) {
            return ApiResponse.builder()
                    .message("Un Authorization")
                    .errors(true)
                    .status(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }

        // Get post with uuid
        Posts posts = postsRepository.findByUuid(id);

        if (posts == null) {
            return ApiResponse.builder()
                    .message("Data not found")
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }

        List<Medias> medias = new ArrayList<>();

        for (PostMediaUpdateRequest item : data.getMedias()) {
            if (item.getId() != null) {
                Medias mediasNeedUpdateIndex = mediasRepository.findById(item.getId())
                        .orElse(null);

                if (mediasNeedUpdateIndex != null) {
                    mediasNeedUpdateIndex.setIndex(item.getIndex());
                    mediasRepository.save(mediasNeedUpdateIndex);
                }

            }

            if (item.getFile() != null && item.getId() == null) {
                Boolean isVideo = item.getFile().getContentType().equalsIgnoreCase("video/mp4");

                File file = ImageUtils.createFileAndSave("medias\\", item.getFile(),
                        OptionCreateAndSaveFile.builder()
                                .acceptExtentions(Constant.ACCEPT_EXTENTION)
                                .build());
//                boolean hasVideo = medias.stream().anyMatch(Medias::getIsVideo);

                if (isVideo) {
                    Medias exitMedias = mediasRepository.existsVideoOfPost(posts);
                    if (exitMedias == null) {
                        mediasRepository.findMediasWithPost(posts)
                                .forEach(exitMedia -> {
                                    ImageUtils.deleteImg("medias/"
                                            + exitMedia.getName());
                                    mediasRepository.delete(exitMedia);
                                });
                    }
                    //Handle when exist video in post
//                    if (hasVideo) {
//                        return ApiResponse.builder()
//                                .message("One post allows only 1 video")
//                                .errors(true)
//                                .status(HttpStatus.BAD_REQUEST.value())
//                                .data(null)
//                                .build();
//                    }

                }

                medias.add(Medias.builder()
                        .index(item.getIndex())
                        .isVideo(isVideo)
                        .post(posts)
                        .name(file == null ? "" : file.getName())
                        .build());
            }

        }

        if (!medias.isEmpty()) {
            posts.setMedias(medias);
        }

        if (posts.getMedias().isEmpty()) {
            return ApiResponse.builder()
                    .message("Update failure Make sure you have submitted the correct data")
                    .errors(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .build();
        }

        posts.setTitle(data.getTitle());
        posts.setLastUpdate(new Date());
        postsRepository.save(posts);

        return ApiResponse.builder()
                .message("Successfuly")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(posts)
                .build();
    }

    @Override
    public ApiResponse delete(String uuid, String token) {
        if(uuid==null||uuid.isEmpty()){
            return ApiResponse.builder()
                    .message("Data not found")
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }
        if(token==null||token.isEmpty()){
            return ApiResponse.builder()
                    .message("Please login to delete")
                    .errors(true)
                    .status(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }
        Posts posts=postsRepository.findByUuid(uuid);
        User user=userService.getUserFromToken(token);
        if(posts==null||user==null){
            return ApiResponse.builder()
                    .message("Data not found")
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }
        if(!posts.getUser().getId().equals(user.getId())&&!userService.isAdmin(user)){
            return ApiResponse.builder()
                    .message("You are not authorized to delete this post")
                    .errors(true)
                    .status(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }
        posts.getMedias().forEach(item->{
            ImageUtils.deleteImg("medias/"+item.getName());
        });
        postsRepository.delete(posts);
        return ApiResponse.builder()
                .message("Successfuly")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(null)
                .build();

    }

    @Override
    public ApiResponse likePost(String uuid, String token) {
        if(token==null){
            return ApiResponse.builder()
                    .message("Please login to like")
                    .errors(true)
                    .status(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }
        if(uuid==null){
            return ApiResponse.builder()
                    .message("Data not found")
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }
        User user=userService.getUserFromToken(token);
        Posts posts=postsRepository.findByUuid(uuid);
        if(user==null||posts==null){
            return ApiResponse.builder()
                    .message("Something not found")
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }
        Likes checkLikes=likeRepository.existByUserAndPost(user.getId(),posts.getId());
        if(checkLikes!=null){
            likeRepository.delete(checkLikes);
            return ApiResponse.builder()
                    .message("Unlike successfully")
                    .errors(false)
                    .status(HttpStatus.OK.value())
                    .data(null)
                    .build();
        }
        Likes like=Likes.builder()
                .user(user)
                .post(posts)
                .build();
        if (like==null){
            return ApiResponse.builder()
                    .message("Like failure")
                    .errors(true)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .build();
        }
        likeRepository.save(like);

        return ApiResponse.builder()
                .message("Like successfully")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(null)
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
                    System.out.println("Can not upload more video");
                    return true;
                }
            }
        }
        return false;
    }
}
