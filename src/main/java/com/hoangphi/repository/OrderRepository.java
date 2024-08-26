package com.hoangphi.repository;

import com.hoangphi.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    @Query(nativeQuery = true, value = "select o.*,o.shipping_info_id as orderShipId from orders o  " +
            "inner join shipping_info si on si.id = o.shipping_info_id " +
            "where [user_id] = :userId and o.[status] like %:status% " +
            "order by " +
            "create_at desc")
    public List<Orders> orderHistory(@Param("userId") String userId, @Param("status") String status);

    @Query(nativeQuery = true, value = "select * from orders " +
            "where [user_id] = :userId")
    public List<Orders> getOrderListByUserID(@Param("userId") String userId);

    @Query("SELECT o FROM Orders o " +
            "WHERE (:username IS NULL OR o.user.username like %:username%) " +
            "AND (:orderId IS NULL OR o.id = :orderId) " +
            "AND (:status IS NULL OR o.status like %:status%) " +
            "AND ((:minDate IS NULL AND :maxDate IS NULL) OR (o.createAt BETWEEN :minDate AND :maxDate)) "
            +
            "ORDER BY " +
            "CASE WHEN :sort = 'total-desc' THEN o.total END DESC, " +
            "CASE WHEN :sort = 'total-asc' THEN o.total END ASC, " +
            "CASE WHEN :sort = 'id-desc' THEN o.id END DESC, " +
            "CASE WHEN :sort = 'id-asc' THEN o.id END ASC, " +
            "CASE WHEN :sort = 'date-desc' THEN o.createAt END DESC, " +
            "CASE WHEN :sort = 'date-asc' THEN o.createAt END ASC, " +
            "o.createAt DESC")
    List<Orders> filterOrders(
            @Param("username") String username,
            @Param("orderId") Integer orderId,
            @Param("status") String status,
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate,
            @Param("sort") String sort);
}
