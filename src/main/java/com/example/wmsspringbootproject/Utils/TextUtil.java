package com.example.wmsspringbootproject.Utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TextUtil {

    private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static boolean textIsEmpty(Object str){
        String tempStr=String.valueOf(str);
        return tempStr.isEmpty()||tempStr.isBlank();
    }

    public static String formatDate(Date date,String pattern){
        if(!textIsEmpty(pattern)){
            simpleDateFormat.applyPattern(pattern);
        }
        return simpleDateFormat.format(date);
    }

    public static String formatDate(Date date){
        return simpleDateFormat.format(date);
    }
}
