package com.hoangphi.repository;

import com.hoangphi.entity.social.Medias;
import com.hoangphi.entity.social.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediasRepository extends JpaRepository<Medias, Integer>{
    @Query("select m from Medias m where m.post=:post order by m.index")
    List<Medias> findMediasWithPost(@Param("post") Posts posts);
    @Query("select m from Medias m where m.name=:name")
    Medias findByName(@Param("name") String name);

    @Query("select m from Medias m where m.post=:post and m.isVideo=true")
    Medias existsVideoOfPost(@Param("post") Posts post);
}
