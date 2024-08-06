package com.hoangphi.repository;

import com.hoangphi.entity.social.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Integer>{
    @Query(nativeQuery = true,value="select * from comments where comments.post_id=:postId and comments.reply_to =:commentId")
    List<Comments> findByReply(@Param("postId") Integer postId, @Param("commentId") Integer commentId);
}
