package com.hoangphi.utils;

import com.hoangphi.constant.Constant;
import com.hoangphi.request.payments.MoMoPaymentRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class MoMoUtils {
    public static String getMoMoPayment(MoMoPaymentRequest data)  {
        String getUrl = "";
        try{
            String extraData = "";
            String rawSignature = "accessKey=" + Constant.MOMO_ACCESS_KEY +
                    "&amount=" + String.valueOf(data.getAmount()) +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + Constant.MOMO_IPN_URL +
                    "&orderId=" + data.getOrderId() +
                    "&orderInfo=" + data.getOrderInfo() +
                    "&partnerCode=" + Constant.MOMO_PARTNER_CODE +
                    "&redirectUrl=" + Constant.MOMO_RETURN_URL +
                    "&requestId=" + data.getOrderId() +
                    "&requestType=" + Constant.MOMO_REQUEST_TYPE;
            String signature = HmacSHA512.hmacSHA512(Constant.SECRET_KEY, rawSignature);
            Map<String, Object> requestBody = getStringObjectMap(data, extraData, signature);
            RestTemplate restTemplate = new RestTemplate();

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create HTTP entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Send request using RestTemplate
            ResponseEntity<String> response = restTemplate.exchange(Constant.MOMO_URL, HttpMethod.POST, entity, String.class);
            getUrl = response.getBody();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return getUrl;
    }

    private static Map<String, Object> getStringObjectMap(MoMoPaymentRequest data, String extraData, String signature) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", Constant.MOMO_PARTNER_CODE);
        requestBody.put("partnerName", "Test");
        requestBody.put("storeId", "MomoTestStore");
        requestBody.put("requestId", data.getOrderId());
        requestBody.put("amount", String.valueOf(data.getAmount()));
        requestBody.put("orderId", data.getOrderId());
        requestBody.put("orderInfo",  data.getOrderInfo());
        requestBody.put("redirectUrl", Constant.MOMO_RETURN_URL);
        requestBody.put("ipnUrl", Constant.MOMO_IPN_URL);
        requestBody.put("lang", "vi");
        requestBody.put("requestType",  Constant.MOMO_REQUEST_TYPE);
        requestBody.put("autoCapture", true);
        requestBody.put("extraData", extraData);
        requestBody.put("signature", signature);
        return requestBody;
    }
}
