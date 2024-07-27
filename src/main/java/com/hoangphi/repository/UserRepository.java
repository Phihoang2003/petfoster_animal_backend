package com.hoangphi.repository;

import com.hoangphi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    User findByUuid(String uuid);
    @Query("select u from Users u where u.email = :email")
    public Optional<User> findByEmail(@Param("email") String email);

    @Query("select u from Users u where u.username = :username")
    public Optional<User> findByUsername(@Param("username") String username);
    @Query(nativeQuery = true,value="select * from Users where Users.token = :token")
    public User findByToken(@Param("token") String token);

}
