package com.hoangphi.service.impl.order;

import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.OrderDetail;
import com.hoangphi.entity.Orders;
import com.hoangphi.entity.Payment;
import com.hoangphi.entity.ShippingInfo;
import com.hoangphi.repository.OrderRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.orders_history.OrderDetailsResponse;
import com.hoangphi.response.orders_history.OrderFilterResponse;
import com.hoangphi.response.orders_history.OrderProductItem;
import com.hoangphi.response.orders_history.OrdersFilterResponse;
import com.hoangphi.service.admin.order.OrderFilterService;
import com.hoangphi.utils.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderFilterServiceImpl implements OrderFilterService {
    private final OrderRepository orderRepository;
    private final FormatUtils formatUtils;
    private final OrderServiceImpl orderServiceImpl;
    @Override
    public ApiResponse filterOrders(Optional<String> username, Optional<Integer> orderId,
                                    Optional<String> status, Optional<LocalDate> minDate,
                                    Optional<LocalDate> maxDate, Optional<String> sort,
                                    Optional<Integer> page, Optional<Boolean> read) {
        LocalDate minDateValue = minDate.orElse(null);
        LocalDate maxDateValue = maxDate.orElse(null);

        if (minDateValue == null && maxDateValue != null) {
            minDateValue = maxDateValue;
        }

        if (maxDateValue == null && minDateValue != null) {
            maxDateValue = minDateValue;
        }

        if (minDateValue != null) {
            if (minDateValue.isAfter(maxDateValue)) {
                return ApiResponse.builder()
                        .message("The max date must after the min date!!!")
                        .status(HttpStatus.CONFLICT.value())
                        .errors("The max date must after the min date!!!")
                        .build();
            }
        }
        List<Orders> orders = orderRepository.filterOrders(username.orElse(null), orderId.orElse(null),
                status.orElse(null), minDateValue, maxDateValue, sort.orElse(null));
        // Filter unread orders if read is true
        if (read.orElse(false)) {
            orders = orderRepository.filterOrders(username.orElse(null), orderId.orElse(null),
                    status.orElse(null), minDateValue, maxDateValue, sort.orElse(null)).stream().filter(item -> {
                return !item.getRead();
            }).toList();
        }
        Pageable pageable = PageRequest.of(page.orElse(0), 10);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), orders.size());

        if (startIndex > endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        List<Orders> visibleOrders = orders.subList(startIndex, endIndex);
        Page<Orders> pagination = new PageImpl<Orders>(visibleOrders, pageable, orders.size());

        List<OrderFilterResponse> orderFilters = new ArrayList<>();
        pagination.getContent().forEach((order) -> {
            orderFilters.add(
                    OrderFilterResponse.builder()
                            .orderId(order.getId())
                            .username(order.getUser().getDisplayName() == null ? order.getUser().getUsername()
                                    : order.getUser().getDisplayName())
                            .total(order.getTotal().intValue())
                            .placedDate(formatUtils.dateToString(order.getCreateAt(), "yyyy-MM-dd"))
                            .status(order.getStatus())
                            .read(order.getRead())
                            .print(order.getPrint())
                            .token(order.getGhnCode())
                            .build());
        });

        return ApiResponse.builder()
                .message("Successfully!!!")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(OrdersFilterResponse.builder()
                        .orderFilters(orderFilters)
                        .pages(pagination.getTotalPages())
                        .build())
                .build();
    }

    @Override
    public ApiResponse orderDetails(Integer id) {
        Orders order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ApiResponse.builder()
                    .message("Order not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors("Order not found")
                    .build();
        }

        ShippingInfo shippingInfo = order.getShippingInfo();
        Payment payment = order.getPayment();

        List<OrderDetail> details = order.getOrderDetails();
        List<OrderProductItem> products = new ArrayList<>();
        details.forEach(item -> {
            products.add(orderServiceImpl.createOrderProductItem(item));
        });

        OrderDetailsResponse orderDetails = OrderDetailsResponse.builder()
                .id(id)
                .address(formatUtils.getAddress(shippingInfo.getAddress(), shippingInfo.getWard(),
                        shippingInfo.getDistrict(),
                        shippingInfo.getProvince()))
                .placedDate(formatUtils.dateToString(order.getCreateAt(), "MMM d, yyyy"))
                .deliveryMethod(shippingInfo.getDeliveryCompany().getCompany())
                .name(shippingInfo.getFullName())
                .paymentMethod(payment.getPaymentMethod().getMethod())
                .phone(shippingInfo.getPhone())
                .products(products)
                .shippingFee(shippingInfo.getShipFee())
                .subTotal(order.getTotal().intValue())
                .description(order.getDescriptions() == null ? "" : order.getDescriptions())
                .total(order.getTotal().intValue() + shippingInfo.getShipFee())
                .state(order.getStatus())
                .expectedTime(order.getExpectedDeliveryTime())
                .username(order.getUser().getUsername())
                .displayName(order.getUser().getDisplayName())
                .print(order.getPrint())
                .read(order.getRead())
                .token(order.getGhnCode())
                .build();

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(orderDetails)
                .build();
    }
}
