package com.hoangphi.utils;

import com.hoangphi.constant.Constant;
import com.hoangphi.request.payments.MoMoPaymentRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class MoMoUtils {
    public static String getMoMoPayment(MoMoPaymentRequest data)  {
        Map<String, String> result = new HashMap<>();
//        String baseOrderId = "ORDER" + System.currentTimeMillis();
        String orderId ="ORDER"+ data.getOrderId();
        String amount=String.valueOf(data.getAmount());
        String orderGroupId = "";
        try{
            String extraData = "";
            String rawSignature = "accessKey=" + Constant.MOMO_ACCESS_KEY +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + Constant.MOMO_IPN_URL +
                    "&orderId=" + orderId +
                    "&orderInfo=" + data.getOrderInfo() +
                    "&partnerCode=" + Constant.MOMO_PARTNER_CODE +
                    "&redirectUrl=" + Constant.MOMO_RETURN_URL +
                    "&requestId=" + orderId +
                    "&requestType=" + Constant.MOMO_REQUEST_TYPE;
            String signature = generateSignature(rawSignature, Constant.MOMO_SECRET_KEY);
            Map<String, Object> requestBody = getStringObjectMap(data,orderId, extraData,orderGroupId, signature);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(Constant.MOMO_URL, entity, Map.class);
            result = response.getBody();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        assert result != null;
        return result.get("payUrl");
    }

    private static Map<String, Object> getStringObjectMap(MoMoPaymentRequest data,String orderId,
                                                          String extraData,String orderGroupId, String signature) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", Constant.MOMO_PARTNER_CODE);
        requestBody.put("partnerName", "Test");
        requestBody.put("storeId", "MomoTestStore");
        requestBody.put("requestId", orderId);
        requestBody.put("amount", String.valueOf(data.getAmount()));
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo",  data.getOrderInfo());
        requestBody.put("redirectUrl", Constant.MOMO_RETURN_URL);
        requestBody.put("ipnUrl", Constant.MOMO_IPN_URL);
        requestBody.put("lang", "vi");
        requestBody.put("requestType",  Constant.MOMO_REQUEST_TYPE);
        requestBody.put("autoCapture", true);
        requestBody.put("extraData", extraData);
        requestBody.put("orderGroupId",orderGroupId);
        requestBody.put("signature", signature);
        return requestBody;
    }
    public static String generateSignature(String data, String key) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : signedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC SHA256 signature", e);
        }
    }
}
