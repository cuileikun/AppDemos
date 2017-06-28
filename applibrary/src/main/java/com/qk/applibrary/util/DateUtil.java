package com.qk.applibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 作者：zhoubenhua
 * 时间：2016-10-31 11:07
 * 功能:日期工具
 */
public class DateUtil {

    public static String getCurrentDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    /**
     * 将秒转换年月日 时分秒
     * @param dateTimes
     * @return
     */
    public static String getDateTimeByTimes(String dateTimes) {
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = null;
        try {
            date = df.format(df.parse(dateTimes));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
