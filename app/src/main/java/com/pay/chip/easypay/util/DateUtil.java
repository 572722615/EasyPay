package com.pay.chip.easypay.util;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/3/23 0023.
 */
public class DateUtil {


    public static boolean compareTime(String s1, String s2) {
        DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = java.util.Calendar.getInstance();
        Calendar c2 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        if (result == 0)
            return true;
        else if (result < 0)
            return false;
        else
            return true;

    }
}
