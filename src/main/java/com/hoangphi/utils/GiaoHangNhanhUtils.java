package com.hoangphi.utils;

import com.hoangphi.constant.Constant;
import com.hoangphi.entity.OrderDetail;
import com.hoangphi.entity.Orders;
import com.hoangphi.entity.ShippingInfo;
import com.hoangphi.request.shipping.ShippingProductRequest;
import com.hoangphi.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GiaoHangNhanhUtils {
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
        return null;
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
