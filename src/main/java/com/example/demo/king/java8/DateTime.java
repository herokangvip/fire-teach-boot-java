package com.example.demo.king.java8;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTime {
    public static void main(String[] args) {
        Long timestamp = System.currentTimeMillis();
        System.out.println(timestamp);
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        int year = localDateTime.getYear();
        int month = localDateTime.getMonth().getValue();
        int day = localDateTime.getDayOfMonth();
        String dateTimeStr;
        if(month<10){
            dateTimeStr = year+"-0"+month+"-"+day +" "+"09:00:00";
        }else {
            dateTimeStr = year+"-"+month+"-"+day +" "+"09:00:00";
        }
        System.out.println(dateTimeStr);

        //字符串转时间
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, df);
        System.out.println(dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }
}
