package com.hoangphi.repository;

import com.hoangphi.entity.Donate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonateRepository extends JpaRepository<Donate,Integer> {
}
