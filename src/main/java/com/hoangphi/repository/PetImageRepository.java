package com.hoangphi.repository;

import com.hoangphi.entity.PetImgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetImageRepository extends JpaRepository<PetImgs, Integer>{
}
