package com.hoangphi.repository;

import com.hoangphi.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, String>{
    @Query(nativeQuery = true,value = "select p.*, p.pet_id as petIdAlias from pet p join favourite f on p.pet_id=f.pet_id where f.user_id=:userId order by id desc ")
    List<Pet> getFavoritePets(@Param("userId") String userId);

    @Query(nativeQuery = true,value = "select top 8 * from pet where (breed_id=:breedId or age=:size) and pet_id!=:petId")
    List<Pet> findByPetStyleAndIgnorePetId(@Param("breedId") String breedId, @Param("size") String size, @Param("petId") String petId);


    @Query("SELECT p "+
            "FROM Pet p "+
            "INNER JOIN p.petBreed pb "+
            "INNER JOIN pb.petType pt "+
            "WHERE (:typeName IS NULL OR pt.name LIKE %:typeName%) "+
            "AND (:name IS NULL OR p.petName like %:name%) "+
            "AND (:colors IS NULL OR (CONCAT(',', :colors, ',') LIKE CONCAT('%,', p.petColor, ',%'))) "+
            "AND (:age IS NULL OR p.age like %:age%) "+
            "AND (:gender IS NULL OR p.sex = :gender) "+
            "ORDER BY "+
            "CASE WHEN :sort = 'oldest' THEN p.fosterAt END ASC, "+
            "CASE WHEN :sort = 'latest' THEN p.fosterAt END DESC"
    )
    List<Pet> filterPets(@Param("name") String name, @Param("typeName") String typeName,
                         @Param("colors") String colors,
                         @Param("age") String age, @Param("gender") Boolean gender, @Param("sort") String sort);

}
