package com.hoangphi.repository;

import com.hoangphi.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Integer> {
    @Query("SELECT s FROM SearchHistory s WHERE s.user.id = :userId ORDER BY s.searchAt DESC")
    public Optional<List<SearchHistory>> findByUserId(@Param("userId") String userId);

    @Query("SELECT s FROM SearchHistory s WHERE s.user.id = :userId AND s.keyword = :keyword")
    public Optional<List<SearchHistory>> findByUserIdAndKeyword(@Param("userId") String userId,@Param("keyword") String keyword);
}
