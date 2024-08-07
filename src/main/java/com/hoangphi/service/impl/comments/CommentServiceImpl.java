package com.hoangphi.service.impl.comments;

import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.User;
import com.hoangphi.entity.social.Comments;
import com.hoangphi.entity.social.LikedComments;
import com.hoangphi.entity.social.Posts;
import com.hoangphi.repository.CommentRepository;
import com.hoangphi.repository.LikeCommentRepository;
import com.hoangphi.repository.PostRepository;
import com.hoangphi.request.comments.CommentPostRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.comments.CommentResponse;
import com.hoangphi.service.comments.CommentService;
import com.hoangphi.service.impl.users.UserServiceImpl;
import com.hoangphi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository   postRepository;
    private final UserService userService;
    private final UserServiceImpl userServiceImpl;
    private final LikeCommentRepository likeCommentRepository;

    @Override
    public ApiResponse commentPost(CommentPostRequest commentPostRequest, String token) {
        if(commentPostRequest==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(null)
                    .errors(true)
                    .build();
        }
        if(token==null){
            return ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Please login to comment!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        Posts posts=postRepository.findByUuid(commentPostRequest.getUuid());
        User user=userService.getUserFromToken(token);
        if (posts == null || user == null) {
            return ApiResponse.builder()
                    .message("Something not found")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        Comments comments=Comments.builder()
                .comment(commentPostRequest.getComment())
                .post(posts)
                .user(user)
                .replyTo(commentPostRequest.getReplyId())
                .build();
        if(comments==null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Something wrong")
                    .data(null)
                    .errors(true)
                    .build();
        }
        commentRepository.save(comments);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Comment successfully!")
                .data(buildCommentResponse(comments))
                .errors(false)
                .build();

    }

    @Override
    public ApiResponse likeComment(Integer commentId, String token) {
        if(commentId==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(null)
                    .errors(true)
                    .build();
        }
        if(token==null){
            return ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Please login to like comment!")
                    .data(null)
                    .errors(true)
                    .build();
        }
        User user=userService.getUserFromToken(token);
        Comments comments=commentRepository.findById(commentId).orElse(null);
        if (comments == null || user == null) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        LikedComments checkLike=likeCommentRepository.existByUserAndComment(user.getId(),comments.getId());
        if(checkLike!=null){
            likeCommentRepository.delete(checkLike);
            return ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Unlike comment successfully!")
                    .data(null)
                    .errors(false)
                    .build();
        }
        LikedComments likedComments=LikedComments.builder()
                .comment(comments)
                .user(user)
                .build();
        if(likedComments==null){
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Something wrong")
                    .data(null)
                    .errors(true)
                    .build();
        }
        likeCommentRepository.save(likedComments);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Like comment successfully!")
                .data(null)
                .errors(false)
                .build();

    }

    public CommentResponse buildCommentResponse(Comments comments){
        String token= ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getHeader("Authorization");
        boolean isLike=false;
        boolean owner=false;
        List<Comments> children=new ArrayList<>();
        if(token!=null){
            User user=userService.getUserFromToken(token);
            isLike=likeCommentRepository.existByUserAndComment(user.getId(),comments.getId())!=null;
            owner = user.getUsername().equals(comments.getUser().getUsername()) || userService.isAdmin(user);

        }
        List<Comments> commentReplies=commentRepository.findByReply(comments.getPost().getId(),comments.getId());
        if(commentReplies!=null){
            children=commentReplies;
        }
        return CommentResponse.builder()
                .id(comments.getId())
                .user(userServiceImpl.buildUserProfileResponse(comments.getUser()))
                .comment(comments.getComment())
                .likes(comments.getLikedComments()!=null?comments.getLikedComments().size():0)
                .isLike(isLike)
                .createAt(comments.getCreateAt())
                .children(buildCommentResponses(children))
                .owner(owner)
                .build();

    }
    public List<CommentResponse> buildCommentResponses(List<Comments> comments){
        return comments.stream().map(this::buildCommentResponse).toList();
    }

}
