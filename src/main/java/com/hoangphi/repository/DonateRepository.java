package com.hoangphi.repository;

import com.hoangphi.entity.Donate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonateRepository extends JpaRepository<Donate,Integer> {
    @Query(value = "select d from Donate d ORDER BY d.donateAt desc")
    public List<Donate> findAllReverse();
}
