package com.example.wmsspringbootproject.Utils;

import cn.hutool.core.util.CharUtil;
import com.example.wmsspringbootproject.common.result.PageResult;
import org.springframework.stereotype.Component;

import java.text.ParseException;
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

    public static Date parestDate(String date,String pattern){
        try{
            if(!textIsEmpty(pattern)){
                simpleDateFormat.applyPattern(pattern);
            }
            return simpleDateFormat.parse(date);
        }catch (ParseException pe){
            System.out.println(pe.getMessage());
            return null;
        }
    }

    public static String removeHtml(String content) {
        return content.replace("<", "《").replace(">", "》");
    }

    public static String formatDate(Date date){
        return simpleDateFormat.format(date);
    }

    /**
     * @apiNote 获取任意时间之前的时间
     * @param time 毫秒数
     * @param targetTime 相较于的时间 默认是当前时间
     * @return 格式化之后的时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getBeforeAnyTime(Date targetTime,Long time){
        if(targetTime==null){
            targetTime=new Date();
        }
        long resultTime=targetTime.getTime()-time;
        return formatDate(new Date(resultTime));
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
    public static boolean isNotEmpty(Integer str){
        return str!=null && str>=0;
    }
}
