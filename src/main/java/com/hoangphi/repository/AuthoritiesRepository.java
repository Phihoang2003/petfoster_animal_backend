package com.hoangphi.repository;

import com.hoangphi.entity.Authorities;
import com.hoangphi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities,Integer> {
    List<Authorities> findByUser(User user);
}
