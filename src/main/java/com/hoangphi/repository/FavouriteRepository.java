package com.hoangphi.repository;

import com.hoangphi.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Integer>{
    @Query("select favourite from Favourite favourite where favourite.user.id=:userId and favourite.pet.petId=:petId ")
    Favourite existByUserAndPet(@Param("userId") String userId, @Param("petId") String petId);


}
