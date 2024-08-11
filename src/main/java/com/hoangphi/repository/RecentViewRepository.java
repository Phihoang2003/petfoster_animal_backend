package com.hoangphi.repository;

import com.hoangphi.entity.RecentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentViewRepository extends JpaRepository<RecentView, Integer>{
    @Query(nativeQuery = true,value = "select * from RecentView rv inner join User u on rv.user_id=u.user_id where rv.user_id=:userId order by view_at desc")
    public List<RecentView> findAll(@Param("userId") String userId);
 }
