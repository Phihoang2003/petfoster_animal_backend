package com.hoangphi.utils;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class FormatUtils {
    public String getAddress(String street, String ward, String district, String province) {
        return String.join(", ", street, ward, district, province);
    }
}
