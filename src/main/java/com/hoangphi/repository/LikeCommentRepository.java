package com.hoangphi.repository;

import com.hoangphi.entity.social.LikedComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeCommentRepository extends JpaRepository<LikedComments,Integer> {
    @Query(value = "select l from LikedComment l where l.user.id=:userId and l.comment.id=:commentId")
    LikedComments existByUserAndComment(@Param("userId") String userId,@Param("commentId") Integer commentId);
}
