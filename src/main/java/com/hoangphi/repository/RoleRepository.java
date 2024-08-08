package com.hoangphi.repository;

import com.hoangphi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(nativeQuery = true,value="select * from role where id=4")
    public Role getRoleUser();

    @Query(nativeQuery = true,value="select * from role where id in (1,2)")
    List<Role> managementRoles();
}
