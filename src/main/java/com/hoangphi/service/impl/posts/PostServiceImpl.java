package com.hoangphi.service.impl.posts;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.constant.Constant;
import com.hoangphi.constant.RespMessage;
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
import com.hoangphi.response.common.PaginationResponse;
import com.hoangphi.response.posts.PostDetailResponse;
import com.hoangphi.response.posts.PostReponse;
import com.hoangphi.service.impl.users.UserServiceImpl;
import com.hoangphi.service.posts.PostService;
import com.hoangphi.service.user.UserService;
import com.hoangphi.utils.ImageUtils;
import com.hoangphi.utils.parent.OptionCreateAndSaveFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserService userService;
    private final PostRepository postsRepository;
    private final MediasRepository mediasRepository;
    private final LikeRepository likeRepository;
    private final JwtProvider   jwtProvider;

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

    @Override
    public List<PostReponse> buildPostHomePageResponses(List<Posts> posts) {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getHeader("Authorization");

        return posts.stream().map(post->{
            boolean isLike=false;
            User user=userService.getUserFromToken(token);
            if(token!=null||user!=null){
                isLike=likeRepository.existByUserAndPost(user.getId(),post.getId())!=null;

            }
            Medias medias=mediasRepository.findMediasWithPost(post).get(0);
            return PostReponse.builder()
                    .id(post.getUuid())
                    .title(post.getTitle())
//                    .thumbnail(portUltil.getUrlImage(medias.getName(), "medias"))
                    .containVideo(medias.getIsVideo())
                    .comments(post.getComments().size())
                    .likes(post.getLikes().size())
                    .isLike(isLike)
//                    .user(userServiceImpl.buildUserProfileResponse(post.getUser()))
                    .build();

        }).toList();

    }

    @Override
    public ApiResponse posts(Optional<String> search, Optional<Integer> page) {
        List<Posts> posts=postsRepository.posts(search);
        if(posts.isEmpty()){
            return ApiResponse.builder()
                    .message("Failure")
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }
        //On one page only show some posts,so we need to paginate
        Pageable pageable= PageRequest.of(page.orElse(0),10);
        int startIndex=(int) pageable.getOffset();
        int endIndex=Math.min((startIndex+pageable.getPageSize()),posts.size());

        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(PaginationResponse.builder().data(new ArrayList<>()).pages(0).build())
                    .errors(false)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        List<Posts> visiblePosts=posts.subList(startIndex,endIndex);
        if (visiblePosts == null) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(PaginationResponse.builder().data(new ArrayList<>()).pages(0).build())
                    .errors(false)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        Page<Posts> pagination = new PageImpl<Posts>(visiblePosts, pageable,
                posts.size());

        return ApiResponse.builder()
                .message("Succeessfuly")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(PaginationResponse.builder().data(visiblePosts)
                        .pages(pagination.getTotalPages()).build())
                .build();
    }

    public List<Posts> getListPostOfUser(String username,Optional<String> rawType){
        String token= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        String type=rawType.orElse(null);
        if(type!=null&&type.equals("likes")){
            if(token!=null){
                String usernameFromToken=jwtProvider.getUsernameFromToken(token);
                if(usernameFromToken.equals(username)){
                    return postsRepository.postsLikeOfUser(username);
                }
            }
            return new ArrayList<>();
        }
        if(type!=null&& type.equals("posts")){
            return postsRepository.postOfUser(username);
        }

        return postsRepository.postOfUser(username);
    }
    @Override
    public ApiResponse postsOfUser(String username, Optional<Integer> page, Optional<String> type) {
        List<Posts> posts =this.getListPostOfUser(username,type);
        if(posts.isEmpty()){
            return ApiResponse.builder()
                    .message("Failure")
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }
        //On one page only show some posts,so we need to paginate
        Pageable pageable= PageRequest.of(page.orElse(0),10);
        int startIndex=(int) pageable.getOffset();
        int endIndex=Math.min((startIndex+pageable.getPageSize()),posts.size());

        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(PaginationResponse.builder().data(new ArrayList<>()).pages(0).build())
                    .errors(false)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        List<Posts> visiblePosts=posts.subList(startIndex,endIndex);
        if (visiblePosts == null) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(PaginationResponse.builder().data(new ArrayList<>()).pages(0).build())
                    .errors(false)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        Page<Posts> pagination = new PageImpl<Posts>(visiblePosts, pageable,
                posts.size());

        return ApiResponse.builder()
                .message("Succeessfuly")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(PaginationResponse.builder().data(visiblePosts)
                        .pages(pagination.getTotalPages()).build())
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
