package com.hoangphi.repository;

import com.hoangphi.entity.social.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository  extends JpaRepository<Likes, Integer>{

    @Query("select l from Likes l where l.user.id=:userId and l.post.id=:postId")
    Likes existByUserAndPost(@Param("userId") String userId,@Param("postId") Integer postId);
}
