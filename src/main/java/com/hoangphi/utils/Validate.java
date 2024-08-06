package com.hoangphi.utils;

import java.net.URL;

public class Validate {
    public static boolean isUrl(String url){
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
