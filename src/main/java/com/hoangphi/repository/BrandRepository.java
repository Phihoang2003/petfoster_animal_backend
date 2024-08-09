package com.hoangphi.repository;

import com.hoangphi.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>{
    @Query("select b from Brand b where deleted = false or deleted is null")
    public List<Brand> findAll();

    @Query(nativeQuery = true, value = "select * from brand where brand= :name and (deleted = 0 or deleted is null)")
    public Optional<List<Brand>> findByName(@Param("name") String name);
}
