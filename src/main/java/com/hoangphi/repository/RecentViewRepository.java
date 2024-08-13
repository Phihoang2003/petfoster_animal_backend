package com.hoangphi.repository;

import com.hoangphi.entity.RecentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentViewRepository extends JpaRepository<RecentView, Integer>{
    @Query(nativeQuery = true, value = "select rv.*, u.user_id as userIdAlias from recent_view rv inner join users u on rv.user_id = u.user_id where rv.user_id = :userId order by rv.view_at desc")
    public List<RecentView> findAll(@Param("userId") String userId);


    @Query(nativeQuery = true, value = "select top 1 rv.*, u.user_id as userIdAlias from recent_view rv inner join users u on rv.user_id = u.user_id where rv.user_id = :userId and rv.product_id = :productId")
    public RecentView existByProductId(@Param("userId") String userId, @Param("productId") String productId);

}
