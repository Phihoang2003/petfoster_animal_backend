package com.hoangphi.service.order;
import com.hoangphi.request.order.UpdateStatusRequest;
import com.hoangphi.response.ApiResponse;


public interface AdminOrderService  {
    public ApiResponse updateOrderStatus(Integer id, UpdateStatusRequest updateStatusRequest);

    public ApiResponse updateReadForOrder(Integer id);

    public ApiResponse updatePrintForOrder(Integer id);
}
