package com.hoangphi.repository;

import com.hoangphi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(nativeQuery = true,value="select * from role where id=4")
    public Role getRoleUser();
}
