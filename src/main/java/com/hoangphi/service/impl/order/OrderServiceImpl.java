package com.hoangphi.service.impl.order;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.constant.Constant;
import com.hoangphi.constant.OrderStatus;
import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.*;
import com.hoangphi.repository.*;
import com.hoangphi.request.order.OrderItem;
import com.hoangphi.request.order.OrderRequest;
import com.hoangphi.request.order.UpdateStatusRequest;
import com.hoangphi.request.payments.MoMoPaymentRequest;
import com.hoangphi.request.payments.PaymentRequest;
import com.hoangphi.request.payments.VnPaymentRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.orders.OrderResponse;
import com.hoangphi.response.orders_history.OrderDetailsResponse;
import com.hoangphi.response.orders_history.OrderHistory;
import com.hoangphi.response.orders_history.OrderHistoryResponse;
import com.hoangphi.response.orders_history.OrderProductItem;
import com.hoangphi.service.image.ImageServiceUtils;
import com.hoangphi.service.order.OrderService;
import com.hoangphi.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final SecurityUtils securityUtils;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ProductRepoRepository productRepoRepository;
    private final ShippingInfoRepository shippingInfoRepository;
    private final DeliveryCompanyRepository deliveryCompanyRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final GiaoHangNhanhUtils giaoHangNhanhUltils;
    private final FormatUtils formatUtils;
    private final ReviewRepository reviewRepository;
    private final HttpServletRequest httpServletRequest;
    private final ImageServiceUtils imageServiceUtils;

    @Override
    public ApiResponse order(String jwt, OrderRequest orderRequest) {
        Double total=0.0;
        Map<String,String> errorsMap=new HashMap< >();
        User user=userRepository.findByUsername(securityUtils.getCurrentUsername()).orElse(null);
        if(user==null){
            errorsMap.put("user","User not found");
            return ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthenticated")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        Addresses addresses=addressRepository.findById(orderRequest.getAddressId()).orElse(null);
        if(addresses==null){
            errorsMap.put("address","Address not found");
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Address not found")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        if(!user.getAddresses().contains(addresses)){
            errorsMap.put("address","This address not found in address list of this user");
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("This address not found in address list of this user")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        ShippingInfo shippingInfo=createShippingInfo(addresses,orderRequest);
        PaymentMethod paymentMethod=paymentMethodRepository.findById(orderRequest.getMethodId()).orElse(null);
        if(paymentMethod==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Payment method not found")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        DeliveryCompany deliveryCompany=deliveryCompanyRepository.findById(orderRequest.getDeliveryId()).orElse(null);
        if(deliveryCompany==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Delivery company not found")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        Payment payment=createPayment(orderRequest);
        Orders orders=createOrder(addresses,payment,shippingInfo);
        List<OrderDetail> orderDetails=new ArrayList<>();
        for (OrderItem orderItem : orderRequest.getOrderItems()) {
            ProductRepo productRepo = productRepoRepository.findProductRepoByIdAndSize(orderItem.getProductId(),
                    orderItem.getSize());

            if (orderItem.getQuantity() <= 0) {
                errorsMap.put("quantity", "quantity must larger than 0");
                return ApiResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("quantity must larger than 0")
                        .errors(errorsMap)
                        .build();
            }

            if (productRepo.getQuantity() < orderItem.getQuantity()) {
                errorsMap.put("quantity", "quantity are not enought, please try another one!!!");
                return ApiResponse.builder()
                        .message(HttpStatus.BAD_REQUEST.toString())
                        .errors(errorsMap)
                        .build();
            }

            OrderDetail orderDetail = this.createOrderDetail(productRepo, orderItem, orders);
            orderDetails.add(orderDetail);
            total += orderDetail.getTotal();
        }
        orders.setTotal(total);
        orders.setOrderDetails(orderDetails);
        orderRepository.save(orders);

        payment.setAmount(orders.getTotal()+shippingInfo.getShipFee());
        paymentRepository.save(payment);
        String paymentUrl;
        if (orderRequest.getMethodId() == 1) {
            orders.setStatus(OrderStatus.PLACED.getValue());

            for (OrderDetail orderDetail : orderDetails) {
                ProductRepo productRepo = orderDetail.getProductRepo();
                updateQuantity(productRepo, orderDetail.getQuantity());
            }

            if (deliveryCompany.getId() == 2) {
                ApiResponse apiResponse = giaoHangNhanhUltils.create(orders);
                if (apiResponse.getErrors().equals(true)) {
                    return apiResponse;
                }

                if (apiResponse.getStatus() == 400) {
                    return apiResponse;
                }
            }
            return ApiResponse.builder()
                    .message("Order successfully!!!")
                    .status(HttpStatus.CREATED.value())
                    .errors(false)
                    .data(orders.getId())
                    .build();
        }else if(orderRequest.getMethodId()==2){
            try{
                paymentUrl= VnPayUtils.getVnPayPayment(VnPaymentRequest.builder()
                        .amounts(payment.getAmount().intValue())
                        .idOrder(orders.getId().toString())
                        .orderInfo("Thanh toan don hang:"+orders.getId())
                        .httpServletRequest(httpServletRequest)
                        .build());
                orders.setStatus(OrderStatus.WAITING.getValue());
                orderRepository.save(orders);

            }catch(Exception e){
                return ApiResponse.builder()
                        .message("Unsupported encoding exception")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errors(true)
                        .build();
            }
            return ApiResponse.builder()
                    .message("Order successfully!!!")
                    .status(HttpStatus.OK.value())
                    .errors(false)
                    .data(paymentUrl)
                    .build();
        }else{
            try{
                paymentUrl= MoMoUtils.getMoMoPayment(MoMoPaymentRequest.builder()
                        .amount(payment.getAmount().intValue())
                        .orderId(orders.getId().toString())
                        .orderInfo("Thanh toan don hang: "+orders.getId())
                        .build());
                orders.setStatus(OrderStatus.WAITING.getValue());
                orderRepository.save(orders);

            }catch(Exception e){
                return ApiResponse.builder()
                        .message("Unsupported encoding exception")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errors(true)
                        .build();
            }
            return ApiResponse.builder()
                    .message("Order successfully!!!")
                    .status(HttpStatus.OK.value())
                    .errors(false)
                    .data(paymentUrl)
                    .build();
        }
    }

    @Override
    public ApiResponse payment(PaymentRequest paymentRequest) {
        Map<String,String> errorsMap=new HashMap<>();
        Orders orders=orderRepository.findById(paymentRequest.getOrderId()).orElse(null);
        if(orders==null){
            errorsMap.put("order","Order not found");
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Order not found")
                    .data(null)
                    .errors(errorsMap)
                    .build();
        }
        Payment payment=orders.getPayment();
        if(paymentRequest.getIsPaid()){
            payment.setAmount(paymentRequest.getAmount().doubleValue());
            payment.setIsPaid(paymentRequest.getIsPaid());
            try {
                // Chuyển đổi chuỗi với định dạng có cả ngày và thời gian "yyyyMMddHHmmss"
                payment.setPayAt(formatUtils.convertStringToLocalDateTime(paymentRequest.getPayAt(), "yyyyMMddHHmmss"));
            } catch (Exception e) {
                errorsMap.put("payAt", "Number Format Exception");
                return ApiResponse.builder()
                        .message("Number Format Exception")
                        .status(HttpStatus.CONFLICT.value())
                        .errors(errorsMap)
                        .data(null).build();
            }
            payment.setTransactionNumber(paymentRequest.getTransactionNumber().toString());
            paymentRepository.save(payment);
            orders.setStatus(OrderStatus.PLACED.getValue());
            orderRepository.save(orders);
            List<OrderDetail> orderDetails=orders.getOrderDetails();
            orderDetails.forEach(orderDetail -> {
                ProductRepo productRepo=orderDetail.getProductRepo();
                updateQuantity(productRepo,orderDetail.getQuantity());
            });
            if (orders.getShippingInfo().getDeliveryCompany().getId() == 2) {
                ApiResponse apiResponse = giaoHangNhanhUltils.create(orders);
                if (apiResponse.getErrors().equals(true)) {
                    return apiResponse;
                }
            }
            return ApiResponse.builder()
                    .message("Order successfully!!!")
                    .status(HttpStatus.CREATED.value())
                    .errors(false)
                    .data(OrderResponse.builder()
                            .orderId(orders.getId())
                            .photoUrl(imageServiceUtils.getImage(
                                    orders.getOrderDetails()
                                            .get(0)
                                            .getProductRepo()
                                            .getProduct()
                                            .getImgs()
                                            .stream()
                                            .findFirst()
                                            .map(Imgs::getNameImg)
                                            .orElse("defaultImageName")))
                            .build())
                    .build();
        }

        return ApiResponse.builder()
                .message("Transaction failure!!! Please try again")
                .status(HttpStatus.FAILED_DEPENDENCY.value())
                .errors(true)
                .data(null)
                .build();
    }

    @Override
    public ApiResponse orderDetails(String jwt, Integer id) {
        User user=userRepository.findByUsername(securityUtils.getCurrentUsername()).orElse(null);
        if(user==null){
            return ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthenticated")
                    .data(null)
                    .errors(null)
                    .build();
        }
        Orders orders=orderRepository.findById(id).orElse(null);
        if(orders==null){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Order not found")
                    .data(null)
                    .errors(null)
                    .build();
        }
        if(!user.getOrders().contains(orders)){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("This order not found in order list of this user")
                    .data(null)
                    .errors(null)
                    .build();
        }
        ShippingInfo shippingInfo = orders.getShippingInfo();
        Payment payment = orders.getPayment();
        List<OrderDetail> details = orders.getOrderDetails();
        List<OrderProductItem> products = new ArrayList<>();
        details.forEach(item -> {
            products.add(this.createOrderProductItem(item));
        });
        OrderDetailsResponse orderDetailsResponse=OrderDetailsResponse.builder()
                .id(id)
                .address(formatUtils.getAddress(shippingInfo.getAddress(), shippingInfo.getWard(),
                        shippingInfo.getDistrict(),
                        shippingInfo.getProvince()))
                .placedDate(formatUtils.dateToString(orders.getCreateAt(), "MMM d, yyyy"))
                .deliveryMethod(shippingInfo.getDeliveryCompany().getCompany())
                .name(shippingInfo.getFullName())
                .paymentMethod(payment.getPaymentMethod().getMethod())
                .phone(shippingInfo.getPhone())
                .products(products)
                .shippingFee(shippingInfo.getShipFee())
                .subTotal(orders.getTotal().intValue())
                .total(orders.getTotal().intValue() + shippingInfo.getShipFee())
                .state(orders.getStatus())
                .expectedTime(orders.getExpectedDeliveryTime())
                .build();

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(orderDetailsResponse)
                .build();
    }

    @Override
    public ApiResponse orderHistory(String jwt, Optional<Integer> page, Optional<String> status) {
        User user=userRepository.findByUsername(securityUtils.getCurrentUsername()).orElse(null);
        if(user==null){
            return ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthenticated")
                    .data(null)
                    .errors(null)
                    .build();
        }
        List<Orders> orders_history=orderRepository.orderHistory(user.getId(),status.orElse(""));
        if(orders_history.isEmpty()){
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("No data available")
                    .data(null)
                    .errors(null)
                    .build();
        }
        List<OrderHistory> data = new ArrayList<>();
        data=orders_history.stream().map(order->{
            List<OrderDetail> orderDetails=order.getOrderDetails();
            List<OrderProductItem> products=new ArrayList<>();
            AtomicReference<Boolean> isTotalRate= new AtomicReference<>(true);
            orderDetails.forEach(orderDetail->{
                OrderProductItem orderProductItem=createOrderProductItem(orderDetail);
                products.add(orderProductItem);
                isTotalRate.set(isTotalRate.get() && orderProductItem.getIsRate());
            });
            return  OrderHistory.builder()
                    .id(order.getId())
                    .datePlace(formatUtils.dateToString(order.getCreateAt(), "MMM d, yyyy"))
                    .state(order.getStatus())
                    .stateMessage(order.getStatus())
                    .total(order.getTotal() + order.getShippingInfo().getShipFee())
                    .products(products)
                    .isTotalRate(isTotalRate.get())
                    .build();
        }).toList();

        Pageable pageable = PageRequest.of(page.orElse(0), 8);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), data.size());

        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        List<OrderHistory> visibleProducts = data.subList(startIndex, endIndex);
        Page<OrderHistory> pagination = new PageImpl<OrderHistory>(visibleProducts, pageable, data.size());

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(OrderHistoryResponse.builder()
                        .data(pagination.getContent())
                        .pages(pagination.getTotalPages())
                        .build())
                .build();
    }

    @Override
    public List<OrderDetailsResponse> orderDetailsTable(String username) {
        List<Orders> orderList = new ArrayList<>();

        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            orderList = orderRepository.getOrderListByUserID(user.getId());
        }

//        orderList = ordersRepository.findAll();
        if (orderList.isEmpty()) {
            return null;
        }

        List<OrderDetailsResponse> oDetailsList = new ArrayList<>();
        oDetailsList=orderList.stream().map(order->{
            ShippingInfo shippingInfo = order.getShippingInfo();
            Payment payment = order.getPayment();
            List<OrderDetail> details = order.getOrderDetails();
            List<OrderProductItem> products = new ArrayList<>();
            details.forEach(item -> {
                products.add(this.createOrderProductItem(item));
            });

            OrderDetailsResponse orderDetails = new OrderDetailsResponse();
            orderDetails.setId(order.getShippingInfo().getId());
            orderDetails.setAddress(formatUtils.getAddress(shippingInfo.getAddress(), shippingInfo.getWard(),
                    shippingInfo.getDistrict(), shippingInfo.getProvince()));
            orderDetails.setPlacedDate(order.getCreateAt().toString());
            orderDetails.setDeliveryMethod(shippingInfo.getDeliveryCompany().getCompany());
            orderDetails.setName(shippingInfo.getFullName());
            orderDetails.setPaymentMethod(payment.getPaymentMethod().getMethod());
            orderDetails.setPhone(shippingInfo.getPhone());
            orderDetails.setProducts(products);
            orderDetails.setShippingFee(shippingInfo.getShipFee());
            orderDetails.setSubTotal(order.getTotal().intValue());
            orderDetails.setTotal(order.getTotal().intValue() + shippingInfo.getShipFee());
            orderDetails.setState(order.getStatus());
            orderDetails.setQuantity(order.getOrderDetails().get(0).getQuantity());
            return orderDetails;
        }).toList();
        return oDetailsList;
    }

    @Override
    public ApiResponse cancelOrder(String jwt, Integer id, UpdateStatusRequest updateStatusRequest) {
        User user = userRepository.findByUsername(securityUtils.getCurrentUsername()).orElse(null);
        if (user == null) {
            return ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthenticated")
                    .data(null)
                    .errors(true)
                    .build();
        }
        Orders orders = orderRepository.findById(id).orElse(null);
        if (orders == null) {
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Order not found")
                    .data(null)
                    .errors(true)
                    .build();
        }
        if (!user.getOrders().contains(orders)) {
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("This order not found in order list of this user")
                    .data(null)
                    .errors(true)
                    .build();
        }
        String updateStatus;
        try {
            updateStatus = OrderStatus.valueOf(updateStatusRequest.getStatus()).getValue();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .message(updateStatusRequest.getStatus() + " doesn't exists in the enum")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(true)
                    .build();
        }
        if (!updateStatus.equalsIgnoreCase(OrderStatus.CANCELLED_BY_CUSTOMER.getValue())) {
            return ApiResponse.builder()
                    .message("You cannot update the order!!!")
                    .status(HttpStatus.FAILED_DEPENDENCY.value())
                    .errors(true)
                    .build();
        }
        if (orders.getStatus().equalsIgnoreCase(OrderStatus.SHIPPING.getValue()) ||
                orders.getStatus().equalsIgnoreCase(OrderStatus.DELIVERED.getValue())) {
            return ApiResponse.builder()
                    .message("You cannot cancel the order!!!")
                    .status(HttpStatus.FAILED_DEPENDENCY.value())
                    .errors(true)
                    .build();
        }
        orders.setStatus(updateStatus);
        orders.setDescriptions(updateStatusRequest.getReason() != null ? updateStatusRequest.getReason() : "");
        orderRepository.save(orders);
        if (orders.getGhnCode() != null) {
            List<String> orderCodes = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();
            orderCodes.add(orders.getGhnCode());
            HttpEntity<Map<String, Object>> request = giaoHangNhanhUltils.createRequest("order_codes", orderCodes);
            ResponseEntity<String> response = restTemplate.postForEntity(Constant.GHN_CANCEL, request, String.class);
        }
        orders.getOrderDetails().forEach(orderDetail -> {
            ProductRepo productRepo = orderDetail.getProductRepo();
            returnQuantity(productRepo, orderDetail.getQuantity());
        });
        return ApiResponse.builder()
                .message("Successfully!!!")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(null)
                .build();
    }

    @Override
    public OrderDetailsResponse printInvoice(Integer id) {
        Orders orders = orderRepository.findById(id).orElse(null);
        if (orders == null)
            return null;
        // set print
        orders.setPrint(orders.getPrint() + 1);

        orderRepository.save(orders);

        ShippingInfo shippingInfo = orders.getShippingInfo();

        Payment payment = orders.getPayment();
        List<OrderDetail> details = orders.getOrderDetails();
        List<OrderProductItem> products = new ArrayList<>();
        details.forEach(item -> {
            products.add(this.createOrderProductItem(item));
        });

        OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
        orderDetailsResponse.setId(orders.getId());
        orderDetailsResponse.setAddress(formatUtils.getAddress(shippingInfo.getAddress(), shippingInfo.getWard(),
                shippingInfo.getDistrict(), shippingInfo.getProvince()));
        orderDetailsResponse.setPlacedDate(formatUtils.dateToString(orders.getCreateAt(), "dd/MM/yyyy"));
        orderDetailsResponse.setDeliveryMethod(shippingInfo.getDeliveryCompany().getCompany());
        orderDetailsResponse.setName(shippingInfo.getFullName());
        orderDetailsResponse.setPaymentMethod(payment.getPaymentMethod().getMethod());
        orderDetailsResponse.setPhone(shippingInfo.getPhone());
        orderDetailsResponse.setProducts(products);
        orderDetailsResponse.setShippingFee(shippingInfo.getShipFee());
        orderDetailsResponse.setSubTotal(orders.getTotal().intValue());
        orderDetailsResponse.setTotal(orders.getTotal().intValue() + shippingInfo.getShipFee());
        orderDetailsResponse.setState(orders.getStatus());
        orderDetailsResponse.setQuantity(orders.getOrderDetails().get(0).getQuantity());
        orderDetailsResponse.setDisplayName(orders.getUser().getFullname() == null ? orders.getUser().getDisplayName()
                : orders.getUser().getFullname());
        return orderDetailsResponse;
    }

    private ShippingInfo createShippingInfo(Addresses addresses,OrderRequest orderRequest){
        return shippingInfoRepository.save(ShippingInfo.builder()
                .fullName(addresses.getRecipient())
                .district(addresses.getDistrict())
                .province(addresses.getProvince())
                .ward(addresses.getWard())
                .address(addresses.getAddress())
                .phone(addresses.getPhone())
                .shipFee(orderRequest.getShip())
                .deliveryCompany(deliveryCompanyRepository.findById(orderRequest.getDeliveryId()).get())
                .build());
    }
    private Payment createPayment(OrderRequest orderRequest){
        return paymentRepository.save(Payment.builder()
                .isPaid(false)
                .paymentMethod(paymentMethodRepository.findById(orderRequest.getMethodId()).get())
                .build());
    }
    private Orders createOrder(Addresses addresses,Payment payment,ShippingInfo shippingInfo){
        return orderRepository.save(Orders.builder()
                .user(addresses.getUser())
                .payment(payment)
                .read(false)
                .print(0)
                .shippingInfo(shippingInfo)
                .build());
    }
    public OrderProductItem createOrderProductItem(OrderDetail orderDetail){
        String image = "";

        if (!orderDetail.getProductRepo().getProduct().getImgs().isEmpty()) {
            image = orderDetail.getProductRepo().getProduct().getImgs().get(0).getNameImg();
        }

        Review review = reviewRepository.findReviewByUserAndProduct(orderDetail.getOrder().getUser().getId(),
                orderDetail.getProductRepo().getProduct().getId(), orderDetail.getOrder().getId()).orElse(null);

        return OrderProductItem.builder()
                .productId(orderDetail.getProductRepo().getProduct().getId())
                .size(orderDetail.getProductRepo().getSize())
                .image(imageServiceUtils.getImage(image))
                .name(orderDetail.getProductRepo().getProduct().getName())
                .brand(orderDetail.getProductRepo().getProduct().getBrand().getBrand())
                .price(orderDetail.getProductRepo().getOutPrice().intValue())
                .quantity(orderDetail.getQuantity())
                .isRate(review != null)
                .repo(orderDetail.getProductRepo().getQuantity())
                .build();
    }
    private OrderDetail createOrderDetail(ProductRepo productRepo, OrderItem orderItem,Orders orders){
        return orderDetailRepository.save(OrderDetail.builder()
                .price(productRepo.getOutPrice())
                .quantity(orderItem.getQuantity())
                .total(productRepo.getOutPrice()*orderItem.getQuantity())
                .productRepo(productRepo)
                .order(orders)
                .build());
    }
    public ProductRepo updateQuantity(ProductRepo productRepo,Integer quantity){
        productRepo.setQuantity(productRepo.getQuantity()-quantity);
        return productRepoRepository.save(productRepo);
    }
    public ProductRepo returnQuantity(ProductRepo productRepo, Integer quantity) {
        productRepo.setQuantity(productRepo.getQuantity() + quantity);
        return productRepoRepository.save(productRepo);
    }
}
