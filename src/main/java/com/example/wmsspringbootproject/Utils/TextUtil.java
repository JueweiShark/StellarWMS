package com.example.wmsspringbootproject.Utils;

import cn.hutool.core.util.CharUtil;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TextUtil {

    private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static boolean textIsEmpty(Object str){
        String tempStr=String.valueOf(str);
        return tempStr.isEmpty() || tempStr.isBlank()|| str==null;
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

    public static String toCamelCase(CharSequence name,char symbol) {
        if (null == name) {
            return null;
        }

        final String name2 = name.toString();
        if (contains(name2, symbol)) {
            final int length = name2.length();
            final StringBuilder sb = new StringBuilder(length);
            boolean upperCase = false;
            for (int i = 0; i < length; i++) {
                char c = name2.charAt(i);

                if (c == symbol) {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        } else {
            return name2;
        }
    }

    public static int indexOf(final CharSequence str, char searchChar, int start, int end) {
        if (isEmpty(str)) {
            return -1;
        }
        final int len = str.length();
        if (start < 0 || start > len) {
            start = 0;
        }
        if (end > len || end < 0) {
            end = len;
        }
        for (int i = start; i < end; i++) {
            if (str.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(CharSequence str, char searchChar, int start) {
        if (str instanceof String) {
            return ((String) str).indexOf(searchChar, start);
        } else {
            return indexOf(str, searchChar, start, -1);
        }
    }

    public static int indexOf(final CharSequence str, char searchChar) {
        return indexOf(str, searchChar, 0);
    }

    public static boolean contains(CharSequence str, char searchChar) {
        return indexOf(str, searchChar) > -1;
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
//    验证int数据不为空
    public static boolean isNotEmpty(Integer str){
        return str!=null && str>=0;
    }
}
