package com.hoangphi.repository;

import com.hoangphi.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {
    @Query(value = "select f from Feedback f ORDER BY f.id desc")
    List<Feedback> findAllReverse();
}
