package com.hoangphi.service.admin.order;

import com.hoangphi.response.ApiResponse;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderFilterService {
    ApiResponse filterOrders(Optional<String> username, Optional<Integer> orderId, Optional<String> status,
                             Optional<LocalDate> minDate, Optional<LocalDate> maxDate, Optional<String> sort, Optional<Integer> page,
                             Optional<Boolean> read);

    ApiResponse orderDetails(Integer id);
}
