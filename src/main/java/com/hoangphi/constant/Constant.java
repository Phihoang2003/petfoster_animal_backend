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
}
