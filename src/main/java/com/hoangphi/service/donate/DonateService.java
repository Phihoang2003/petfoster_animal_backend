package com.hoangphi.service.donate;

import com.hoangphi.request.transaction.TransactionRequest;
import com.hoangphi.response.ApiResponse;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DonateService {
    ApiResponse getTransaction(Optional<Integer> page);

    ApiResponse report();

    ApiResponse filterTransaction(Optional<String> search, Optional<Date> minDate, Optional<Date> maxDate,
                                  Optional<String> sort, Optional<Integer> page);

    ApiResponse setTransactionToDB(List<TransactionRequest> transactionRequest);
}
