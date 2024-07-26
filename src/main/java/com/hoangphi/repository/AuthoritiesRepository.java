package com.hoangphi.repository;

import com.hoangphi.entity.Authorities;
import com.hoangphi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities,Integer> {
    List<Authorities> findByUser(User user);
}
