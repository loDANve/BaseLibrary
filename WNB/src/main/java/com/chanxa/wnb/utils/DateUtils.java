package com.chanxa.wnb.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CHANXA on 2015/1/30.
 */
public class DateUtils {
    /**
     * 比较时间
     * @param forMat
     * @param date1
     * @param date2
     * @return 返回时间间隔
     * @throws ParseException
     */
    public static int compare(String forMat, String date1, String date2) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(forMat);
        Date inputDate1 = dateFormat.parse(date1);
        Date inputDate2 = dateFormat.parse(date2);
        compare(inputDate1,inputDate2);
        return 0;
    }

    public static int compare(Date date1, Date date2) throws ParseException {
        return (int) (date1.getTime()-date2.getTime());
    }
}
