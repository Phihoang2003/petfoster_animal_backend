package com.hoangphi.constant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constant {
    public static final String BASE_URL="http://localhost:8019/login/";
    public static final String SECRET_KEY = "dfhdsfjhjdfjsdhfdfhdsfjhjdfjsdhfdfhdsfjhjdfjsdhfdfhdsfjhjdfjsdhfdfhdsfjhjdfjsdhf";
    public static final Integer TOKEN_EXPIRE_LIMIT = 5 * 60 * 1000;
    public static List<String> ACCEPT_EXTENTION = new ArrayList<>(Arrays.asList(
            "svg",
            "webp", "jpg", "png", "mp4"));

    public static LocalDate MIN_DATE = LocalDate.ofEpochDay(0);
    public static String GHN_TOKEN="f970798b-5efb-11ef-8e53-0a00184fe694";
    public static String GHN_SHOPID="194360";
    public static String GHN_GETPROVINCE = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
    public static String GHN_GETDISCTRICT = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
    public static String GHN_GETWARD = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id";
    public static String GHN_CREATE = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
    public static String GHN_CANCEL = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/switch-status/cancel";

    public static String VNP_TMN_CODE = "1ULMXVXE";
    public static String VNP_RETURN_URL = "http://localhost:8019/payment";
    public static String VNP_SECRET_KEY = "RK367TRZDMPVP4NRZ3S5P8TQRBNVJGR1";
    public static String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    public static String MOMO_ACCESS_KEY="F8BBA842ECF85";
    public static String MOMO_SECRET_KEY="K951B6PE1waDMi640xX08PD3vg6EkVlz";
    public static String MOMO_PARTNER_CODE="MOMO";
    public static String MOMO_RETURN_URL = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
    public static String MOMO_IPN_URL = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
    public static String MOMO_REQUEST_TYPE="payWithMethod";
    public static String MOMO_URL = "https://test-payment.momo.vn/v2/gateway/api/create";

//    public static String MOMO_ACCESS_KEY="klm05TvNBzhg7h7j";
//    public static String MOMO_SECRET_KEY="at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa";
//    public static String MOMO_PARTNER_CODE="MOMOBKUN20180529";
//    public static String MOMO_RETURN_URL = "http://localhost:8019/payment";
//    public static String MOMO_IPN_URL = "http://localhost:8019/payment";
//    public static String MOMO_REQUEST_TYPE="payWithMethod";
//    public static String MOMO_URL = "https://test-payment.momo.vn/v2/gateway/api/create";










}
