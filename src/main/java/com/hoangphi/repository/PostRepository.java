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

    @Query("select p from Posts p where (:search IS NULL OR p.title LIKE %:search% OR CAST(p.id AS string) LIKE %:search% OR p.user.username LIKE %:search%) order by p.createAt desc")
    List<Posts> posts(@Param("search") String search);

    @Query(nativeQuery = true,value = "select * from posts where posts.id in (select top 5 p.id from posts p join likes l on l.post_id=p.id group by p.id order by COUNT(*) desc)")
    List<Posts> highlight();

    @Query(nativeQuery = true, value = "select posts.*, u.user_id as userIdAlias from posts join users u on posts.user_id = u.user_id where posts.id in (select top 5 p.id from posts p join likes l on l.post_id = p.id group by p.id order by COUNT(*) desc) and u.username = :username order by posts.create_at desc")
    List<Posts> highlightOfUser(@Param("username") String username);

    @Query(nativeQuery = true, value = "select top 4 * from posts")
    List<Posts> findAllByActive();

}
