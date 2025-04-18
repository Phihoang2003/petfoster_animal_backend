package com.hoangphi.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoangphi.constant.Constant;
import com.hoangphi.entity.OrderDetail;
import com.hoangphi.entity.Orders;
import com.hoangphi.entity.ShippingInfo;
import com.hoangphi.repository.OrderRepository;
import com.hoangphi.request.shipping.GHNPostRequest;
import com.hoangphi.request.shipping.ShippingProductRequest;
import com.hoangphi.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GiaoHangNhanhUtils {
    private final OrderRepository ordersRepository;
    public ApiResponse create(Orders orderRequest) {
        ShippingInfo shippingInfo = orderRequest.getShippingInfo();
        ArrayList<ShippingProductRequest> list = new ArrayList<>();
        int totalWeight = 0;
        for (OrderDetail item : orderRequest.getOrderDetails()) {
            list.add(ShippingProductRequest.builder().name(item.getProductRepo().getProduct().getName()).code(item.getProductRepo().getProduct().getId()).quantity(item.getQuantity()).price(item.getPrice().intValue()).weight(item.getProductRepo().getSize()).build());
            totalWeight += item.getQuantity() * item.getProductRepo().getSize();

        }
        Integer provinceId = getProvinceId(shippingInfo.getProvince());
        if (provinceId == null) {
            return ApiResponse.builder().message("Province name not found").status(404).errors(true).build();
        }
        Integer districtId = getDistrictId(provinceId, shippingInfo.getDistrict());
        if (districtId == null) {
            return ApiResponse.builder().message("District name not found").status(404).errors(true).build();
        }
        String wardId = this.getWardId(shippingInfo.getWard(), districtId);
        if (wardId == null) {
            return ApiResponse.builder().message("Ward name not found").status(404).errors(true).build();
        }

        GHNPostRequest post = GHNPostRequest.builder()
                .to_name(shippingInfo.getFullName())
                .to_phone(shippingInfo.getPhone())
                .to_address(shippingInfo.getAddress())
                .to_ward_code(wardId)
                .to_district_id(districtId)
                .payment_type_id(orderRequest.getPayment().getPaymentMethod().getId() == 2 ? 1 : 2)
                .cod_amount(orderRequest.getPayment().getPaymentMethod().getId() == 2 ? 0 : orderRequest.getTotal().intValue())
                .items(list)
                .weight(totalWeight)
                .build();

        String url = Constant.GHN_CREATE;
        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Token", Constant.GHN_TOKEN);
        headers.set("ShopId", Constant.GHN_SHOPID);

        // create a request
        HttpEntity<GHNPostRequest> request = new HttpEntity<>(post, headers);

        ResponseEntity<String> response = null;
        try {
            // send POST request
            response = restTemplate.postForEntity(url,
                    request, String.class);
        } catch (HttpClientErrorException.BadRequest e) {
            return ApiResponse.builder().message("This province is not support").status(400).errors(true).build();
        } catch (HttpClientErrorException e) {
            // Handle other HttpClientErrorException types if needed
            System.out.println("Caught an HttpClientErrorException: " + e.getMessage());
        } catch (Exception e) {
            // Handle other types of exceptions
            System.out.println("Caught an exception: " + e.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        String code=null;
        try{
            assert response != null;
            JsonNode jsonNode=mapper.readTree(response.getBody());
            String expectedDeliveryTime=jsonNode.path("data").path("expected_delivery_time").asText();
            String ghnCode = jsonNode.path("data").path("order_code").asText();
            // code = jsonNode.path("data").path("code").asInt();

            orderRequest.setExpectedDeliveryTime(expectedDeliveryTime);
            orderRequest.setGhnCode(ghnCode);
            ordersRepository.save(orderRequest);
            code=ghnCode;


        }catch(Exception e){
            e.printStackTrace();
        }
        return ApiResponse.builder()
                .message("Create order success")
                .status(HttpStatus.CREATED.value())
                .errors(false)
                .data(code)
                .build();
    }

    public Integer getProvinceId(String provinceName) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = createRequest("ProvinceName", provinceName);
        ResponseEntity<String> response = restTemplate.exchange(Constant.GHN_GETPROVINCE, HttpMethod.GET, request, String.class);

        JSONArray dataArray = getData(response);
        for (Object item : dataArray) {
            JSONObject object = (JSONObject) item;
            List<Object> names;
            try {
                names = object.getJSONArray("NameExtension").toList();
            } catch (Exception e) {
                continue;
            }
            if (object.getString("ProvinceName").equalsIgnoreCase(provinceName)) {
                return object.getInt("ProvinceID");
            }
            for (Object name : names) {
                if (name.toString().equalsIgnoreCase(provinceName)) {
                    return object.getInt("ProvinceID");
                }
            }
        }
        return null;
    }
    public Integer getDistrictId(Integer provinceId,String districtName){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> request = createRequest("province_id", provinceId);
        ResponseEntity<String> response = restTemplate.exchange(Constant.GHN_GETDISCTRICT, HttpMethod.POST, request,
                String.class);

        JSONArray dataArray = getData(response);

        for (Object item : dataArray) {
            JSONObject object = (JSONObject) item;
            List<Object> names;
            try {
                names = object.getJSONArray("NameExtension").toList();
            } catch (Exception e) {
                continue;
            }

            if(object.getString("DistrictName").equalsIgnoreCase(districtName)) {
                return object.getInt("DistrictID");
            }

            for(Object name : names) {
                if(name.toString().equalsIgnoreCase(districtName)) {
                    return object.getInt("DistrictID");
                }
            }
        }

        return null;
    }
    public String getWardId(String wardName, Integer districtId) {

        // build a request
        HttpEntity<Map<String, Object>> request = this.createRequest("district_id", districtId);

        RestTemplate restTemplate = new RestTemplate();

        // send request
        ResponseEntity<String> response = restTemplate.exchange(Constant.GHN_GETWARD, HttpMethod.POST, request,
                String.class);

        // get data
        JSONArray data = this.getData(response);
        for (Object item : data) {
            JSONObject object = (JSONObject) item;
            List<Object> names;
            try {
                names = object.getJSONArray("NameExtension").toList();
            } catch (Exception e) {
                continue;
            }

            if(object.getString("WardName").equalsIgnoreCase(wardName)) {
                return object.getString("WardCode");
            }

            for(Object name : names) {
                if(name.toString().equalsIgnoreCase(wardName)) {
                    return object.getString("WardCode");
                }
            }
        }

        return null;
    }

    public JSONArray getData(ResponseEntity<String> response) {
        String responseBody = response.getBody();
        JSONObject jsonObject = null;
        if (responseBody != null) {
            jsonObject = new JSONObject(responseBody);
        }

        assert jsonObject != null;
        return jsonObject.getJSONArray("data");
    }

    public HttpEntity<Map<String, Object>> createRequest(String key, Object value) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Token", Constant.GHN_TOKEN);
        headers.set("ShopId", Constant.GHN_SHOPID);

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        return new HttpEntity<>(map, headers);

    }

}


