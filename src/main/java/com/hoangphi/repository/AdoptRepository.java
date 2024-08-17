package com.hoangphi.repository;

import com.hoangphi.entity.Adopt;
import com.hoangphi.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface AdoptRepository extends JpaRepository<Adopt,Integer> {
    @Query(nativeQuery = true, value = "select * from adopt where pet_id = :petId and user_id = :userId and status like 'Waiting'")
    Adopt existsByPetAndUser(@Param("petId") String petId, @Param("userId") String userId);

    // The pet was adopted or registered cannot register adopt
    @Query(nativeQuery = true, value = "select * from adopt where pet_id = :petId and status in ('Adopted', 'Registered')")
    Adopt exitsAdopted(@Param("petId") String petId);

    @Query("select a from Adopt a where a.status = 'Adopted' and a.pet = :pet")
    Adopt findByPetAdopted(@Param("pet") Pet pet);

    @Query(nativeQuery = true,value="select * from adopt a where a.pet_id=:petId and a.status='Waiting' and a.user_id !=:userId")
    List<Adopt> findByUserIgnoreUserId(@Param("userId") String userId, @Param("petId") String petId);

    @Query("select a from Adopt a where a.user.id=:userId order by a.registerAt desc")
    List<Adopt> findByUser(@Param("userId") String userId);

    @Query(nativeQuery = true, value = "select * from adopt where user_id = :userId and adopt_id = :adoptId")
    Adopt existsByUser(@Param("userId") String userId, @Param("adoptId") Integer adoptId);

    @Query("select a from Adopt a where a.user.id=:userId and a.status=:status order by a.registerAt desc")
    List<Adopt> findByUser(@Param("userId") String userId,@Param("status") String status);

    @Query("SELECT a FROM Adopt a " +
            "INNER JOIN a.user u " +
            "INNER JOIN a.pet p " +
            "WHERE (:name IS NULL OR u.displayName like %:name%) " +
            "AND (:petName IS NULL OR p.petName like %:petName%) " +
            "AND ((:registerStart IS NULL AND :registerEnd IS NULL) OR (a.registerAt BETWEEN :registerStart AND :registerEnd)) "
            +
            "AND ((:adoptStart IS NULL AND :adoptEnd IS NULL) OR (a.adoptAt BETWEEN :adoptStart AND :adoptEnd)) "
            +
            "AND (:status IS NULL OR a.status LIKE :status) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'id-asc' THEN a.adoptId END ASC, " +
            "CASE WHEN :sort = 'id-desc' THEN a.adoptId END DESC, " +
            "CASE WHEN :sort = 'pet-asc' THEN p.petId END ASC, " +
            "CASE WHEN :sort = 'pet-desc' THEN p.petId END DESC, " +
            "CASE WHEN :sort = 'adopt-asc' THEN a.adoptAt END ASC, " +
            "CASE WHEN :sort = 'adopt-desc' THEN a.adoptAt END DESC ")
    public List<Adopt> filterAdopts(
            @Param("name") String name,
            @Param("petName") String petName,
            @Param("status") String status,
            @Param("registerStart") LocalDate registerStart,
            @Param("registerEnd") LocalDate registerEnd,
            @Param("adoptStart") LocalDate adoptStart,
            @Param("adoptEnd") LocalDate adoptEnd,
            @Param("sort") String sort);


}
