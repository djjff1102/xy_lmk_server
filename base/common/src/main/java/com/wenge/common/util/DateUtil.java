package com.wenge.common.util;

import com.wenge.common.config.log.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *
 * </p>
 *
 * @Author: liyanxiang
 * @CreateTime: 2023-03-07 14:17
 * @Version: 1.0
 */
@SuppressWarnings("all")
public class DateUtil {

    static private MyDateFormat myDateFormat;

    static private String format = "yyyy-MM-dd";

    static private String storageFormat = "yyyy-MM-dd";

    static private boolean easy = false;

    public static MyDateFormat getMyDateFormat(String format) {
        DateUtil.myDateFormat = new MyDateFormat(format);
        return DateUtil.myDateFormat;
    }

    public static void setParams(String format, String storageFormat, boolean easy) {
        DateUtil.format = format;
        DateUtil.storageFormat = storageFormat;
        DateUtil.easy = easy;
    }

    public static void setParams(String format, String storageFormat) {
        DateUtil.format = format;
        DateUtil.storageFormat = storageFormat;
    }

    /**
     * 格式化Excel时间(yyyy-MM-dd)
     *
     * @param str
     * @return
     */
    public static String formatExcelDate(String str) {
        //数据的格式
        int yearIndex = StringUtils.indexOf(format, "yyyy");
        int monthIndex = StringUtils.indexOf(format, "MM");
        int dayIndex = StringUtils.indexOf(format, "dd");
        String year = format.substring(yearIndex + 4, monthIndex);
        String month = format.substring(monthIndex + 2, dayIndex);
        //存储的格式
        int storageYearIndex = StringUtils.indexOf(storageFormat, "yyyy");
        int storageMonthIndex = StringUtils.indexOf(storageFormat, "MM");
        int storageDayIndex = StringUtils.indexOf(storageFormat, "dd");
        String storageYear = storageFormat.substring(storageYearIndex + 4, storageMonthIndex);
        String storageMonth = storageFormat.substring(storageMonthIndex + 2, storageDayIndex);
        if (StringUtils.isBlank(str)) {
            return "";
        } else if (StringUtils.isNumeric(str)) {
            /*数字*/
            Calendar calendar = new GregorianCalendar(1900, 0, -1);
            Date gregorianDate = calendar.getTime();
            return DateUtil.getMyDateFormat(storageFormat).format(DateUtils.addDays(gregorianDate, Integer.parseInt(str)));
        } else if (str.matches("^(\\d{4}" + year + "\\d{1,2})$") && easy) {
            /*yyyy/MM*/
            Date date = DateUtil.getMyDateFormat("yyyy" + year + "MM").parse(str);
            return DateUtil.getMyDateFormat("yyyy" + storageYear + "MM").format(date);
        } else if (str.matches("^(\\d{4}" + year + "\\d{1,2}" + month + "\\d{1,2})$")) {
            /*yyyy/MM/dd*/
            Date date = DateUtil.getMyDateFormat("yyyy" + year + "MM" + month + "dd").parse(str);
            return DateUtil.getMyDateFormat("yyyy" + storageYear + "MM" + storageMonth + "dd").format(date);
        } else {
            return "";
        }
    }

    public static String formatExcelDate(String str, boolean easyTemp) {
        //数据的格式
        int yearIndex = StringUtils.indexOf(format, "yyyy");
        int monthIndex = StringUtils.indexOf(format, "MM");
        int dayIndex = StringUtils.indexOf(format, "dd");
        String year = format.substring(yearIndex + 4, monthIndex);
        String month = format.substring(monthIndex + 2, dayIndex);
        //存储的格式
        int storageYearIndex = StringUtils.indexOf(storageFormat, "yyyy");
        int storageMonthIndex = StringUtils.indexOf(storageFormat, "MM");
        int storageDayIndex = StringUtils.indexOf(storageFormat, "dd");
        String storageYear = storageFormat.substring(storageYearIndex + 4, storageMonthIndex);
        String storageMonth = storageFormat.substring(storageMonthIndex + 2, storageDayIndex);
        if (StringUtils.isBlank(str)) {
            return "";
        } else if (StringUtils.isNumeric(str)) {
            /*数字*/
            Calendar calendar = new GregorianCalendar(1900, 0, -1);
            Date gregorianDate = calendar.getTime();
            return DateUtil.getMyDateFormat(storageFormat).format(DateUtils.addDays(gregorianDate, Integer.parseInt(str)));
        } else if (str.matches("^(\\d{4}" + year + "\\d{1,2})$") && easyTemp) {
            /*yyyy/MM*/
            Date date = DateUtil.getMyDateFormat("yyyy" + year + "MM").parse(str);
            return DateUtil.getMyDateFormat("yyyy" + storageYear + "MM").format(date);
        } else if (str.matches("^(\\d{4}" + year + "\\d{1,2}" + month + "\\d{1,2})$")) {
            /*yyyy/MM/dd*/
            Date date = DateUtil.getMyDateFormat("yyyy" + year + "MM" + month + "dd").parse(str);
            return DateUtil.getMyDateFormat("yyyy" + storageYear + "MM" + storageMonth + "dd").format(date);
        } else {
            return "";
        }
    }

    /**
     * 自动填充0
     *
     * @param format
     * @param str
     * @return
     */
    public static String autoAdd0(String format, String str) {
        Date date = DateUtil.getMyDateFormat(format).parse(str);
        return DateUtil.getMyDateFormat(format).format(date);
    }

    /*日期内部类*/
    public static class MyDateFormat {
        private SimpleDateFormat simpleDateFormat;

        public MyDateFormat(String format) {
            this.simpleDateFormat = new SimpleDateFormat(format);
        }

        public String format(Date date) {
            if (date == null) {
                return "";
            } else {
                return simpleDateFormat.format(date);
            }
        }

        public Date parse(String str) {
            if (str == null) {
                return null;
            } else {
                try {
                    return simpleDateFormat.parse(str);
                } catch (ParseException e) {
                    return null;
                }
            }
        }
    }
    public static Date parse(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = sdf.parse(str);
        } catch (ParseException e) {
            Log.logger.error(e.getMessage(),e);
        }
        return parse;
    }

    public static Date dataParse(String str) {
        DateUtil.setParams("yyyy/MM/dd","yyyy-MM-dd HH:mm:ss");
        String date = DateUtil.formatExcelDate(str);
        Date parse = parse(date);
        return parse;
    }

    /*日期校验*/
    public static boolean dateVerification(String date){
        String eL= "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(date);
        boolean b = m.matches();
        return b;
    }

    public static void main(String[] args) {
        String date = "2023/06/09";
        DateUtil.setParams("yyyy/MM/dd","yyyy-MM-dd HH:mm:ss");
        String s = DateUtil.formatExcelDate(date);
        System.out.println(s);
    }
}
