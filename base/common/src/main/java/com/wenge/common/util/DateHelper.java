package com.wenge.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;


public class DateHelper {
	public static final String FULL_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String FULL_PATTERN_YEAR = "yyyy-MM-dd";
	private static final ThreadLocal<SimpleDateFormat> safeSimpleDateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}

		;
	};
	private static final ThreadLocal<Calendar> safeCalendar = ThreadLocal.withInitial(Calendar::getInstance);
	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;
	private static final long ONE_DAY = 86400000L;
	private static final long ONE_WEEK = 604800000L;
	private static final String ONE_SECOND_AGO = "秒前";
	private static final String ONE_MINUTE_AGO = "分钟前";
	private static final String ONE_HOUR_AGO = "小时前";
	private static final String ONE_DAY_AGO = "天前";
	private static final String ONE_MONTH_AGO = "月前";
	private static final String ONE_YEAR_AGO = "年前";
	static Logger logger = LoggerFactory.getLogger(DateHelper.class);
	static DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern(FULL_PATTERN);

	/**
	 * 返回days天前的对象
	 *
	 * @return
	 */
	public static Date getDateBeforeHours(int hours) {
		return getDateAfterHours(-hours);
	}

	public static Date getDateBeforeHours(int hours, Date date) {
		return getDateAfterHours(-hours, date);
	}

	public static Date getDateAfterHours(int hours) {
		return getDateAfterHours(hours, System.currentTimeMillis());
	}

	public static Date getDateAfterDays(int days, Date date) {
		return getDateAfterDays(days, date.getTime());
	}

	public static Date getDateAfterDays(int days, long times) {
		return Date.from(LocalDateTime.from(Instant.ofEpochMilli(times).atZone(ZoneId.systemDefault())).plusDays(days).atZone(ZoneId.systemDefault()).toInstant());

	}

	public static Date getDateAfterHours(int hours, Date date) {
		return getDateAfterHours(hours, date.getTime());
	}

	public static Date getDateAfterHours(int hours, long times) {
		return Date.from(LocalDateTime.from(Instant.ofEpochMilli(times).atZone(ZoneId.systemDefault())).plusHours(hours).atZone(ZoneId.systemDefault()).toInstant());

	}

	public static Date getDateBeforeMinutes(int minutes, Date date) {
		return getDateAfterMinutes(-minutes, date.getTime());
	}

	public static Date getDateAfterMinutes(int minutes, long times) {
		return Date.from(LocalDateTime.from(Instant.ofEpochMilli(times).atZone(ZoneId.systemDefault())).plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant());

	}

	/**
	 * 返回days天前的对象
	 *
	 * @param days
	 * @return
	 */
	public static Date getDateBeforeDays(int days) {
		return getDateAfterDays(-days);
	}

	public static Date getDateBeforeMonths(Date date, int month) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime localDateTime1 = localDateTime.minusMonths(month);
		return Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date getDateAfterMonths(Date date, int month) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime localDateTime1 = localDateTime.plusMonths(month);
		return Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 返回days天后的对象
	 *
	 * @param days
	 * @return
	 */
	public static Date getDateAfterDays(int days) {
		return Date.from(LocalDateTime.now().plusDays(days).atZone(ZoneId.systemDefault()).toInstant());

	}

	/**
	 * 验证dateStr格式是否正确
	 *
	 * @param dateStr
	 * @return
	 */
	public static boolean validateDateStr(String dateStr) {
		if (StringUtils.isBlank(dateStr)) {
			return false;
		}

		if (dateStr.split("-")[0].length() > 4) {
			return false;
		}

		try {
			defaultFormatter.parse(dateStr);
		} catch (Exception e) {
			logger.error("", e);
			logger.error("日期格式不正确：" + dateStr);
			return false;
		}
		return true;
	}

	/**
	 * @param dateStr
	 * @return 符合日期格式返回 date对象，否则返回null
	 */


	public static Date str2Date(String dateStr) {
		Date date = null;
		try {
			date = Date.from(LocalDateTime.parse(dateStr, defaultFormatter).atZone(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			logger.error("", e);
			logger.error("date pattern error" + dateStr);
			throw new RuntimeException("错误的日期格式：" + dateStr);
		}
		return date;
	}

	/**
	 * @param date
	 * @return 异常返回 ""，正常返回 yyyy-MM-dd HH:mm:ss 格式日期字符串
	 */


	public static String date2Str(Date date) {
		return date2Str(date, FULL_PATTERN);
	}

	public static String date2Str(Date date, String pattern) {
		if (date == null || StringUtils.isBlank(pattern)) {
			return "";
		}
		return DateTimeFormatter.ofPattern(pattern).format(date.toInstant().atZone(ZoneId.systemDefault()));
	}

	public static String delDate(String startTime) {
		String endTime = startTime.substring(0, 10);
		endTime = endTime.replaceFirst("-", "年");
		endTime = endTime.replaceFirst("-", "月");
		endTime = endTime + "日";
		return endTime;
	}

	public static List<String> delTimeList(List<String> timeListInterval, String interval) {
		List<String> delTimeList = new ArrayList<>();
		for (String time : timeListInterval) {
			time = time.substring(5);
			if (interval.contains("m")) {
				time = time.substring(0, time.length() - 3);
			}
			if (interval.contains("h")) {
				time = time.substring(0, time.length() - 6);
			}
			if (interval.contains("d")) {
				time = time.substring(0, time.length() - 9);
			}
			delTimeList.add(time);
		}
		return delTimeList;
	}

	public static SimpleDateFormat getThreadSafeSimpleDateFormat() {
		return safeSimpleDateFormat.get();
	}

	public static Calendar getThreadSafeCalendar() {
		return safeCalendar.get();
	}

	public static List<String> getDateList(String startTime, String endTime) throws ParseException {
		List<String> times = new ArrayList<>();
		SimpleDateFormat dateFormat = DateHelper.getThreadSafeSimpleDateFormat();
		Date endDate = dateFormat.parse(endTime);
		Date startDate = dateFormat.parse(startTime);
		dateFormat.applyPattern(DateHelper.FULL_PATTERN_YEAR);

		Calendar ca = DateHelper.getThreadSafeCalendar();
		ca.setTime(endDate);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		Date date = endDate;
		while (date.after(startDate)) {
			date = ca.getTime();
			times.add(dateFormat.format(date));
			ca.add(Calendar.DAY_OF_MONTH, -1);
		}
		dateFormat.applyPattern(DateHelper.FULL_PATTERN);
		return times;
	}

	/**
	 * 转换时间为 秒前 分钟前 小时前 天前 月前 年前
	 *
	 * @return
	 */
	public static String relativeDateFormat(Date date) {
		long delta = System.currentTimeMillis() - date.getTime();
		if (delta >= 0) {
			if (delta < 1L * ONE_MINUTE) {
				long seconds = toSeconds(delta);
				return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
			}
			if (delta < 45L * ONE_MINUTE) {
				long minutes = toMinutes(delta);
				return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
			}
			if (delta < 24L * ONE_HOUR) {
				long hours = toHours(delta);
				return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
			}
			if (delta < 48L * ONE_HOUR) {
				return "昨天";
			}
			if (delta < 30L * ONE_DAY) {
				long days = toDays(delta);
				return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
			}
			if (delta < 12L * 4L * ONE_WEEK) {
				long months = toMonths(delta);
				return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
			} else {
				// long years = toYears(delta);
				// return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
				return "历史";
			}
		} else {
			return "";
		}

	}

	private static long toSeconds(long date) {
		return date / 1000L;
	}

	private static long toMinutes(long date) {
		return toSeconds(date) / 60L;
	}

	private static long toHours(long date) {
		return toMinutes(date) / 60L;
	}

	private static long toDays(long date) {
		return toHours(date) / 24L;
	}

	private static long toMonths(long date) {
		return toDays(date) / 30L;
	}

	private static long toYears(long date) {
		return toMonths(date) / 365L;
	}

	public static List<String[]> splitTime(String startTime, String endTime) throws ParseException {
		List<String[]> times = new ArrayList<>();
		Calendar calendar = getThreadSafeCalendar();
		SimpleDateFormat dateFormat = getThreadSafeSimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		Date start = dateFormat.parse(startTime);
		times.add(getstartTimeEndTime(startTime));
		Date end = dateFormat.parse(endTime);
		calendar.setTime(start);

		while (calendar.getTime().before(end)) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			times.add(getstartTimeEndTime(dateFormat.format(calendar.getTime())));
		}
		dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
		return times;
	}

	private static String[] getstartTimeEndTime(String startTime) {
		return new String[]{startTime + " 00:00:00", startTime + " 23:59:59"};
	}

	public static void testDateFormat() throws ParseException {
		SimpleDateFormat dateFormat = getThreadSafeSimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		String format = dateFormat.format(new Date());
		System.out.println(format);
		Date date = dateFormat.parse("2018-11-16");
		System.out.println(date);
	}

	public static void testSplitTimes() throws ParseException {
		List<String[]> splitTime = splitTime("2018-11-10", "2018-11-16");
		printListStrArr(splitTime);

	}

	private static void printListStrArr(List<String[]> splitTime) {
		for (String[] strs : splitTime) {
			System.out.println(strs[0] + "=" + strs[1]);
		}
	}

	public static void testFillTime() {
		List<String> asList = Arrays.asList("2018-11-12 11:00:00", "2018-11-13 12:00:00", "2018-11-14 14:00:00", "2018-11-15 18:00:00");
		List<String[]> fillTime = fillTime(asList, "1h");
		printListStrArr(fillTime);

	}

	/**
	 * 根据时间间隔补全开始结束时间
	 *
	 * @param timeListInterval
	 * @param interval
	 * @return
	 */
	public static List<String[]> fillTime(List<String> timeListInterval, String interval) {
		String startsuf = "";
		String endsuf = "";
		if ("1h".equals(interval)) {
			startsuf = "00:00";
			endsuf = "59:59";
		}
		if ("1d".equals(interval)) {
			startsuf = "00:00:00";
			endsuf = "23:59:59";
		}

		List<String[]> times = new ArrayList<>(timeListInterval.size());
		for (String time : timeListInterval) {
			time = time.substring(0, time.length() - startsuf.length());
			times.add(new String[]{time + startsuf, time + endsuf});
		}
		return times;
	}

	//获取当天零点时间
	public static String getCurrentDateMidnightStr() {
		Date zero = getCurrentDateMidnightDate();

		return DateHelper.date2Str(zero);
	}

	//获取当天零点时间
	public static Date getCurrentDateMidnightDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date addDateInterval(Date date, int field, int i) {
		Calendar threadSafeCalendar = getThreadSafeCalendar();
		threadSafeCalendar.setTime(date);
		threadSafeCalendar.add(field, i);
		Date time = threadSafeCalendar.getTime();
		return time;
	}

	/**
	 * 判断字符串是否是数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static void main(String[] args) {
		System.out.println(DateHelper.date2Str(new Date(), FULL_PATTERN_YEAR + " 00:00:00"));
	}

}
