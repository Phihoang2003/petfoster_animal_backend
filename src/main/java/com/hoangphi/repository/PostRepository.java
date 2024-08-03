package com.hoangphi.repository;

import com.hoangphi.entity.social.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer>{
    Posts findByUuid(String uuid);

    @Query(nativeQuery = true,value = "select * from posts where id in (select l.post_id from likes l join users u on l.[user_id]=u.[user_id] where u.username = :username) order by posts.create_at desc")
    List<Posts> postsLikeOfUser(@Param("username") String username);

    @Query(value = "select p from Posts p where p.user.username = :username order by p.createAt desc")
    List<Posts> postOfUser(String username);

    @Query("select p from Posts p where (:search IS NULL OR p.title LIKE %:search% OR p.id LIKE %:search% OR p.user.username LIKE %:search%) order by p.createAt desc")
    List<Posts> posts(@Param("search") Optional<String> search);

}
