package com.common.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式化工具类
 */
public class DateUtils {
    /**
     * @param format:格式化形式
     * @return：格式化后的字符串
     */
    public static String dateFormat(String format, String milliseconds) {
        if (!TextUtils.isEmpty(format) && !TextUtils.isEmpty(milliseconds)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format.trim());
            return sdf.format(new Date(Long.parseLong(milliseconds.trim())));
        }
        return "";
    }

    /**
     * yyyy.MM.dd HH:mm
     *
     * @param format:格式化形式
     * @return：格式化后的字符串
     */
    public static String dateTimeFormat(String format, String milliseconds) {
        if (!TextUtils.isEmpty(format) && !TextUtils.isEmpty(milliseconds)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(Long.parseLong(milliseconds.trim())));
        }
        return "";
    }

    /**
     * 获取当前时间 yyyy年MM月dd日
     *
     * @return
     */
    public static String getDatedian_yyyy_MM_dd(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return str;
        } else {
            return "";
        }
    }

    /**
     * 获取年
     *
     * @param
     * @return
     */
    public static int getDate_yyyy() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date curDate = new Date();// 获取当前时间
        String str = formatter.format(curDate);
        return Integer.parseInt(str);
    }

    /**
     * 获取年
     *
     * @param timeStr
     * @return
     */
    public static int getDate_yyyy(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }
    /**
     * 获取年
     *
     * @param timeStr
     * @return
     */
    public static String getDate_yyyyAndMM(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return str;
        } else {
            return "";
        }
    }

    /**
     * 获取月
     *
     * @param timeStr
     * @return
     */
    public static int getDate_MM(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }

    /**
     * 获取日
     *
     * @param timeStr
     * @return
     */
    public static int getDate_dd(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }

    /**
     * 获取小时
     *
     * @param timeStr
     * @return
     */
    public static int getDate_HH(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }

    /**
     * 获取月
     *
     * @param timeStr
     * @return
     */
    public static int getDate_mm(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }

    /**
     * 获取月日
     *
     * @param timeStr
     * @return
     */
    public static String getDate_mmdd(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return str;
        } else {
            return "";
        }
    }


    /**
     * 获取小时分钟
     *
     * @param timeStr
     * @return
     */
    public static String getDate_Hm(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        if (timeStr != null && timeStr.length() > 0) {
            Date curDate = new Date(Long.parseLong(timeStr));// 获取当前时间
            String str = formatter.format(curDate);
            return str;
        } else {
            return "";
        }
    }

    /**
     * 将时间字符串转换成毫秒值
     *
     * @param time
     * @param format
     * @return
     */
    public static long dateToMillis(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null)
            return date.getTime();
        else
            return 0;
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime(long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sDateFormat.format(new Date(time));
    }

    /**
     * 计算2个时间戳的天数差。
     *
     * @param endDateStr
     * @param nowDateStr
     * @return
     */
    public synchronized static int getDayDiff(String endDateStr, String nowDateStr) {

        if (TextUtils.isEmpty(endDateStr) || TextUtils.isEmpty(nowDateStr)) {
            return -1;
        }

        long nd = 1000 * 24 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = Long.valueOf(endDateStr) - Long.valueOf(nowDateStr);
        // 计算差多少天
        int day = (int) (diff / nd);
        return day;
    }

    /**
     * 计算2个时间的时间差。
     *
     * @param endDateStr
     * @param nowDateStr
     * @return
     */
    public synchronized static String getDatePoor(String endDateStr, String nowDateStr) {

        if (TextUtils.isEmpty(endDateStr) || TextUtils.isEmpty(nowDateStr)) {
            return null;
        }

        Date endDate = null;
        Date nowDate = null;

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            endDate = sDateFormat.parse(endDateStr);
            nowDate = sDateFormat.parse(nowDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        if (day == 0) {
            if (hour == 0) {
                if (min == 0) {
                    return sec + "秒";
                } else {
                    return min + "分钟" + sec + "秒";
                }
            } else {
                return hour + "小时" + min + "分钟" + sec + "秒";
            }
        } else {
            return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
        }
    }

    /**
     * @param
     * @return
     */
    public static String timeslash(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd,HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static String getTimeShow(int demandType, int startYear, int startMonth, int startDay
            , int startHour, int startMinute, int endYear, int endMonth, int endDay, int endHour, int endMinute) {
        StringBuffer dateStr = new StringBuffer();

        int date_yyyy = getDate_yyyy();
        if(date_yyyy != startYear){
            dateStr.append(startYear + "-");
        }
        if (demandType == 3) {
            dateStr.append(StringUtils.get2Str(startMonth) + "-")
                    .append(StringUtils.get2Str(startDay)).append("  " + StringUtils.get2Str(startHour))
                    .append(":" + StringUtils.get2Str(startMinute));
        } else {
            dateStr.append(StringUtils.get2Str(startMonth))
                    .append("-" + StringUtils.get2Str(startDay));//开始时间一直都是这样 不变
            if (startYear != endYear) {
                dateStr.append("至" + endYear + "-");//如果是跨年发单的话 增加结尾的年份
            } else if (startMonth != endMonth || startDay != endDay) {
                dateStr.append("至");
            }
            if (startYear == endYear&&startMonth == endMonth && startDay == endDay) {

            } else if(startYear == endYear&&startMonth == endMonth){
                dateStr.append(StringUtils.get2Str(endDay));
            } else {
                dateStr.append(StringUtils.get2Str(endMonth) + "-")
                        .append(StringUtils.get2Str(endDay));
            }
            dateStr.append("  ").append(StringUtils.get2Str(startHour)).append(":" + StringUtils.get2Str(startMinute))
                    .append("-" + StringUtils.get2Str(endHour)).append(":" + StringUtils.get2Str(endMinute));
        }
        return dateStr.toString();

    }


    public static String getDateShow(int demandType, int startYear, int startMonth, int startDay
            , int endYear, int endMonth, int endDay) {
        StringBuffer dateStr = new StringBuffer();

        int date_yyyy = getDate_yyyy();
//        if(date_yyyy != startYear){
            dateStr.append(startYear + ".");
//        }
        if (demandType == 3) {
            dateStr.append(StringUtils.get2Str(startMonth) + ".")
                    .append(StringUtils.get2Str(startDay));
        } else {
            dateStr.append(StringUtils.get2Str(startMonth))
                    .append("." + StringUtils.get2Str(startDay));//开始时间一直都是这样 不变
//            if (startYear != endYear) {
                dateStr.append(" - " + endYear + "-")
                        .append(StringUtils.get2Str(endMonth))
                        .append("." + StringUtils.get2Str(endDay))
                ;//如果是跨年发单的话 增加结尾的年份
//            } else if (startMonth != endMonth || startDay != endDay) {
//                dateStr.append("至");
//            }
//            if (startYear == endYear&&startMonth == endMonth && startDay == endDay) {
//
//            } else if(startYear == endYear&&startMonth == endMonth){
//                dateStr.append(StringUtils.get2Str(endDay));
//            } else {
//                dateStr.append(StringUtils.get2Str(endMonth) + "-")
//                        .append(StringUtils.get2Str(endDay));
//            }
//            dateStr.append("  ").append(StringUtils.get2Str(startHour)).append(":" + StringUtils.get2Str(startMinute))
//                    .append("-" + StringUtils.get2Str(endHour)).append(":" + StringUtils.get2Str(endMinute));
        }
        return dateStr.toString();

    }


    public static String getTimeShow(int demandType
            , int startHour, int startMinute, int endHour, int endMinute) {
        StringBuffer dateStr = new StringBuffer();

        if (demandType == 3) {
            dateStr.append(StringUtils.get2Str(startHour))
                    .append(":" + StringUtils.get2Str(startMinute));
        } else {
            dateStr.append(StringUtils.get2Str(startHour)).append(":" + StringUtils.get2Str(startMinute))
                    .append(" - " + StringUtils.get2Str(endHour)).append(":" + StringUtils.get2Str(endMinute));
        }
        return dateStr.toString();

    }

    public static String getTimeShowV(int demandType, int startYear, int startMonth, int startDay
            , int startHour, int startMinute, int endYear, int endMonth, int endDay, int endHour, int endMinute) {
        StringBuffer dateStr = new StringBuffer();

        int date_yyyy = getDate_yyyy();
        if(date_yyyy != startYear){
            dateStr.append(startYear + "年");
        }
        if (demandType == 3) {
            dateStr.append(StringUtils.get2Str(startMonth) + "月")
                    .append(StringUtils.get2Str(startDay)).append("日  " + StringUtils.get2Str(startHour))
                    .append(":" + StringUtils.get2Str(startMinute));
        } else {
            dateStr.append(StringUtils.get2Str(startMonth))
                    .append("月" + StringUtils.get2Str(startDay)+"日");//开始时间一直都是这样 不变
            if (startYear != endYear) {
                dateStr.append("至" + endYear + "年");//如果是跨年发单的话 增加结尾的年份
            } else if (startMonth != endMonth || startDay != endDay) {
                dateStr.append("至");
            }
            if (startYear == endYear&&startMonth == endMonth && startDay == endDay) {

            } else if(startYear == endYear&&startMonth == endMonth){
                dateStr.append(StringUtils.get2Str(endDay));
            } else {
                dateStr.append(StringUtils.get2Str(endMonth) + "月")
                        .append(StringUtils.get2Str(endDay)+"日");
            }
            dateStr.append("  ").append(StringUtils.get2Str(startHour)).append(":" + StringUtils.get2Str(startMinute))
                    .append("-" + StringUtils.get2Str(endHour)).append(":" + StringUtils.get2Str(endMinute));
        }
        return dateStr.toString();

    }

    public static int getHour() {
        SimpleDateFormat sdr = new SimpleDateFormat("HH");
        String format = sdr.format(new Date());
        return Integer.parseInt(format);
    }

    public static int getMinute() {
        SimpleDateFormat sdr = new SimpleDateFormat("mm");
        String format = sdr.format(new Date());
        return Integer.parseInt(format);
    }

    public static long getLong(String date){
        Date dt = new Date(date);
        long lSysTime1 = dt.getTime();   //得到秒数，Date类型的getTime()返回毫秒数
        return lSysTime1;
    }
}
