package com.hoangphi.repository;

import com.hoangphi.entity.Donate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonateRepository extends JpaRepository<Donate,Integer> {
    @Query(value = "select d from Donate d ORDER BY d.donateAt desc")
    public List<Donate> findAllReverse();

    @Query("SELECT SUM(d.donateAmount) FROM Donate d " +
            "WHERE ((:minDate IS NULL AND :maxDate IS NULL) OR (CAST(d.donateAt AS date) BETWEEN :minDate AND :maxDate)) " +
            "AND ( " +
            "  (:reportType = 'day' AND DAY(d.donateAt) = DAY(:date) AND MONTH(d.donateAt) = MONTH(:date) AND YEAR(d.donateAt) = YEAR(:date)) OR " +
            "  (:reportType = 'month' AND MONTH(d.donateAt) = MONTH(:date) AND YEAR(d.donateAt) = YEAR(:date)) OR " +
            "  (:reportType = 'year' AND YEAR(d.donateAt) = YEAR(:date)) " +
            ")")
    public Double report(
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate,
            @Param("date") LocalDate date,
            @Param("reportType") String reportType);

    @Query(value = "select d from Donate d " +
            "where (:search IS NULL OR d.donater LIKE %:search% OR d.beneficiaryBank LIKE %:search% OR d.toAccountNumber LIKE %:search%)"
            +
            "AND ((:minDate IS NULL AND :maxDate IS NULL) OR (d.donateAt BETWEEN :minDate AND :maxDate)) "
            +
            "ORDER BY " +
            "CASE WHEN :sort = 'oldest' THEN d.donateAt END ASC, " +
            "CASE WHEN :sort = 'latest' THEN d.donateAt END DESC ")

    List<Donate> filterAdminDonates(@Param("search") Optional<String> search,
                                    @Param("minDate") Optional<LocalDate> minDate,
                                    @Param("maxDate") Optional<LocalDate> maxDate, @Param("sort") String sort);

    @Query("select sum(d.donateAmount) from Donate d " +
            "where (:search IS NULL OR d.donater LIKE %:search% OR d.beneficiaryBank LIKE %:search% OR d.toAccountNumber LIKE %:search%)"
            +
            "AND ((:minDate IS NULL AND :maxDate IS NULL) OR (d.donateAt BETWEEN :minDate AND :maxDate)) ")
    public Double totalFilterDonation(@Param("search") Optional<String> search,
                                      @Param("minDate") Optional<LocalDate> minDate,
                                      @Param("maxDate") Optional<LocalDate> maxDate);

    @Query("select sum(d.donateAmount) from Donate d")
    public Integer getDonation();
}
