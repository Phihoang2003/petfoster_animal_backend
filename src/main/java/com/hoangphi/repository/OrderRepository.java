package com.hoangphi.repository;

import com.hoangphi.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
