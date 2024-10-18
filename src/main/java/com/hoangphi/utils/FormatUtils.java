package com.hoangphi.utils;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class FormatUtils {
    public String getAddress(String street, String ward, String district, String province) {
        return String.join(", ", street, ward, district, province);
    }
    public String dateToString(LocalDate date, String pattern) {
        DateTimeFormatter formatter = null;
        try {
            formatter = DateTimeFormatter.ofPattern(pattern);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert formatter != null;
        return date.format(formatter);
    }


    public Map<String,LocalDate> changeDateRange(Optional<LocalDate> minDate,Optional<LocalDate> maxDate){
        LocalDate minDateValue=minDate.orElse(null);
        LocalDate maxDateValue=maxDate.orElse(null);
        try{
            if (minDateValue == null && maxDateValue != null) {
                minDateValue = maxDateValue;
            }

            if (maxDateValue == null && minDateValue != null) {
                maxDateValue = minDateValue;
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        Map<String,LocalDate> results=new HashMap<>();
        results.put("minDateValue", minDateValue);
        results.put("maxDateValue", maxDateValue);

        return results;

    }
    public LocalDate dateToDateFormat(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date.format(formatter), formatter);
    }
    public LocalDateTime convertStringToLocalDateTime(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateString, formatter);
    }

}
