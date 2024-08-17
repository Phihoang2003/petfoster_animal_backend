package com.hoangphi.repository;

import com.hoangphi.entity.Adopt;
import com.hoangphi.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
