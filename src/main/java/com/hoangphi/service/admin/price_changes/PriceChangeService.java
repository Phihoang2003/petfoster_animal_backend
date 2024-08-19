package com.hoangphi.service.admin.price_changes;

import com.hoangphi.response.ApiResponse;

public interface PriceChangeService {
    ApiResponse getPriceChange(String idProduct);
}
