package com.hoangphi.repository;

import com.hoangphi.entity.Imgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Imgs, Integer> {
}
