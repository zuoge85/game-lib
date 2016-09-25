package com.isnowfox.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


public final class DateUtils {

    /**
     * 获取一天开始的时间的long
     *
     * @return
     */
    public static final long getDateStartTime(Date d) {
        @SuppressWarnings("deprecation")
        java.sql.Date d2 = new java.sql.Date(d.getYear(), d.getMonth(), d.getDate());
        return d2.getTime();
    }

    public static final long getYearStartTime(Date d) {
        @SuppressWarnings("deprecation")
        java.sql.Date d2 = new java.sql.Date(d.getYear(), 0, 1);
        return d2.getTime();
    }

    public static final int getUtcHours(Date data){
        int hours = (data.getHours() * 60 + data.getMinutes() +data.getTimezoneOffset()) / 60;
        return hours;
    }


    public static final int getUtcMinutes(Date data){
        int minutes = (data.getHours() * 60 + data.getMinutes() +data.getTimezoneOffset()) % 60;
        return minutes;
    }

    public static long parseTime(String startTime) {
        int index = startTime.indexOf(" ");

        int day = NumberUtils.toInt(startTime.substring(0, index), -1);
        if (day == -1) {
            throw new RuntimeException("错误的日期" + startTime);
        }
        long time = day * 24 * 60 * 60 * 1000;

        time += parseMillisOfDay(startTime.substring(index + 1));
        return time;
    }
    
    public static boolean isTheSameDay(long time1, long time2){
    	DateTime t1 = new DateTime(time1);
		DateTime t2 = new DateTime(time2);

		if((t1.getDayOfYear() + t1.getYear() * 1000)  == (t2.getDayOfYear() + t2.getYear() * 1000)){
			return true;
		}
		return false;
    }

    public static int parseMillisOfDay(String startTime) {
        if (StringUtils.isNotEmpty(startTime)) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
            return LocalTime.parse(startTime, fmt).getMillisOfDay();
        } else {
            return 0;
        }
    }
}
