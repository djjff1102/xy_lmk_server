package com.wenge.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

	private static final Logger logger = LoggerFactory.getLogger(TimeUtils.class);
	private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
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
	static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	/**
	 * 时间范围内 支持开始时间小于结束时间情况
	 *
	 * @param startStr
	 * @param endStr
	 * @param nowStr
	 * @return
	 */
	static LocalTime zeroTime = LocalTime.parse("23:59:59", dateTimeFormatter);
	static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("YYYYMM");
	private static Pattern mumPattern = Pattern.compile("\\d+");
	private static DateFormat format = new SimpleDateFormat(FORMAT_PATTERN);
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN);
	private static Map<String, Integer> intervalField = new HashMap<String, Integer>();
	private static Map<String, String> intervalPattren = new HashMap<String, String>();
	private static Map<String, String> intervalCUllTail = new HashMap<String, String>();
	private static Map<String, String> intervalCUllFormat = new HashMap<String, String>();

	static {
		initConfig();
	}

	private static void initConfig() {
		intervalField.put("m", 1000 * 60);
		intervalField.put("h", 1000 * 60 * 60);
		intervalField.put("d", 1000 * 60 * 60 * 24);
		intervalField.put("M", 1000 * 60 * 60 * 24 * 30);

		intervalPattren.put("m", "yyyy-MM-dd HH:mm:00");
		intervalPattren.put("h", "yyyy-MM-dd HH:00:00");

		intervalPattren.put("d", "yyyy-MM-dd 00:00:00");
		intervalPattren.put("M", "yyyy-MM-01");

		intervalCUllTail.put("m", "00");
		intervalCUllTail.put("h", "00:00");
		intervalCUllTail.put("d", "00:00:00");
		intervalCUllTail.put("M", "01 00:00:00");

		intervalCUllFormat.put("m", "ss");
		intervalCUllFormat.put("h", "mm:ss");
		intervalCUllFormat.put("d", "HH:mm:ss");
		intervalCUllFormat.put("M", "dd HH:mm:ss");
	}

	/**
	 * 根据时间间隔和格式化字符串获取日期数组。
	 *
	 * @param sTime
	 * @param eTime
	 * @param millisSecondInterval
	 * @param
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getTimeListInterval(Date sTime, Date eTime, Integer millisSecondInterval, String pattn, String formatData)
			throws ParseException {
		List<String> rt = Collections.emptyList();
		if (sTime == null || eTime == null || millisSecondInterval == null) {
			return rt;
		}
		if (sTime.compareTo(eTime) > 0) {
			throw new RuntimeException("sTime must less than eTime:stime " + sTime + ":eTime" + eTime);
		}
		DateFormat format = new SimpleDateFormat(pattn);

		long sTimeMllis = sTime.getTime();
		long eTimeMllis = eTime.getTime();
		String sTimeS = format.format(sTimeMllis);
		long subMillis = eTimeMllis - sTimeMllis;
		int length = (int) (subMillis / millisSecondInterval);
		if (length == 0) {//处理日期不到时间间隔的问题
			return Arrays.asList(format.format(sTime));
		}
		if (length > 0) {
			rt = new ArrayList<String>(length);
			rt.add(sTimeS);
			LocalDateTime dateTime = LocalDateTime.from(Instant.ofEpochMilli(sTimeMllis).atZone(ZoneId.systemDefault()));
			for (int i = 0; i < length; i++) {
				dateTime = dateTime.plus(millisSecondInterval, ChronoUnit.MILLIS);
				Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
				rt.add(format.format(Date.from(instant)));
			}
		}
		//因为essql是小于，包头不包尾 剔除
		List<String> cull = isCull(formatData, eTime, rt);
		return cull;
	}

	private static List<String> isCull(String format, Date eTime, List<String> list) {
		String date = intervalCUllTail.get(format);
		String result = DateTimeFormatter.ofPattern(intervalCUllFormat.get(format)).format(eTime.toInstant().atZone(ZoneId.systemDefault()));
		if (date.equals(result)) {
			list.remove(DateHelper.date2Str(eTime));
		}
		return list;
	}

	/**
	 * 根据时间间隔和格式化字符串获取日期数组。
	 *
	 * @param sTime
	 * @param eTime
	 * @param millisSecondInterval
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getTimeListIntervalback(Date sTime, Date eTime, Integer millisSecondInterval,
	                                                   String pattern) throws ParseException {
		List<String> rt = Collections.emptyList();
		if (sTime == null || eTime == null || millisSecondInterval == null) {
			return rt;
		}
		if (sTime.compareTo(eTime) > 0) {
			throw new RuntimeException("sTime must less than eTime:stime " + sTime + ":eTime" + eTime);
		}
		DateFormat format = null;
		if (StringUtils.isNotBlank(pattern)) {
			format = new SimpleDateFormat(pattern);
		} else {
			format = TimeUtils.format;
		}
		String sTimeS = format.format(sTime);
		String eTimeS = format.format(eTime);
		long sTimeMllis = format.parse(sTimeS).getTime();
		long eTimeMllis = format.parse(eTimeS).getTime();
		long subMillis = eTimeMllis - sTimeMllis;
		int length = (int) (subMillis / millisSecondInterval);
		if (length > 0) {
			rt = new ArrayList<String>(length);
			rt.add(sTimeS);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(sTimeMllis);
			for (int i = 0; i < length; i++) {
				ca.add(Calendar.MILLISECOND, millisSecondInterval);
				String date = format.format(ca.getTime());
				rt.add(date);
			}
		}
		return rt;
	}

	public static List<String> getTimeListInterval(Date sTime, Date eTime, String interval) throws ParseException {
		if (StringUtils.isBlank(interval)) {
			throw new RuntimeException("interval is not null");
		}

		Matcher matcher = mumPattern.matcher(interval);
		matcher.find();
		int end = matcher.end();
		int num = Integer.parseInt(interval.substring(0, end));
		String patt = interval.substring(end);
		return getTimeListInterval(sTime, eTime, num * intervalField.get(patt), intervalPattren.get(patt), patt);
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
			return "刚刚";
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

	/**
	 * 根据日期获取es分组间隔
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String getInterval(String startTime, String endTime) {

		long smillis = LocalDateTime.parse(startTime, dateFormatter).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long emillis = LocalDateTime.parse(endTime, dateFormatter).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();


		long submillis = emillis - smillis;

		Integer hField = intervalField.get("h");

		Integer h = (int) (submillis / hField);
		if (h < 1) {
			return "1m";
		} else if (h >= 1 && h <= 24) {
			return "1h";
		} else {
			return "1d";
		}
	}

	public static int differentDaysByMillisecond(String datestr1, String datestr2, String interval) {
		int days = -1;

		long divisor = 1;
		String pattern = "";
		if ("1h".equalsIgnoreCase(interval)) {
			divisor = intervalField.get("h");
			pattern = intervalPattren.get("h").substring(0, 13);
		} else if ("1d".equalsIgnoreCase(interval)) {
			divisor = intervalField.get("d");
			pattern = intervalPattren.get("d").substring(0, 10);
		}

		DateFormat format = new SimpleDateFormat(pattern);
		try {
			Date date1 = format.parse(datestr1);
			Date date2 = format.parse(datestr2);
			long instance = date2.getTime() - date1.getTime();
			return (int) (instance / divisor);
		} catch (ParseException e) {
			logger.error("", e);
		}
		return days;
	}

	/**
	 * 获得指定日期的后N天
	 *
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay, int tag) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			logger.error("", e);
			throw new RuntimeException("datString error:", e);
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + tag);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
		return dayAfter;
	}

	public static int getCurrentDatePart(int calendarPart) {
		Calendar threadSafeCalendar = DateHelper.getThreadSafeCalendar();
		threadSafeCalendar.setTime(new Date());

		return threadSafeCalendar.get(calendarPart);
	}

	//获取该日期，N小时之前的时间
	public static String getNhoursBefore(String dateString, Integer hour) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException("datString error:", e);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(calendar.getTime());
	}

	/**
	 * 获取该时间前N分钟
	 */
	public static String getNMinuteBefore(String dateString, Integer minute) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException("datString error:", e);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - minute);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(calendar.getTime());
	}

	public static void main(String[] args) {

		System.out.println(getNMinuteBefore("2020-03-02 15:40:01", 5));
		String st = "2022-05-01 15:00:00";
		String et = "2022-05-20 00:00:00";
		int i = differentDaysByMillisecond(st, et, "1d");
		System.out.println(i);

	}

	public static boolean intime(String startStr, String endStr, String nowStr) {
		LocalTime start = LocalTime.parse(startStr, dateTimeFormatter);
		LocalTime end = LocalTime.parse(endStr, dateTimeFormatter);
		if (start.equals(end)) {
			return true;
		}
		LocalTime now = LocalTime.parse(nowStr, dateTimeFormatter);
		if (start.isBefore(end)) {
			if (now.isAfter(start) && now.isBefore(end)) {
				return true;
			}
		} else {
			if (now.isBefore(zeroTime)) {
				return now.isAfter(start);
			} else {
				return now.isBefore(end);
			}
		}
		return false;
	}

	public static boolean intime(String startStr, String endStr, LocalTime nowTime) {
		String format = dateTimeFormatter.format(nowTime);
		return intime(startStr, endStr, format);
	}

	public static List<String> getAfterMonth(String year, String month, int months) {
		ArrayList<String> objects = new ArrayList<>(months);
		if (month.startsWith("0")) {
			month = month.substring(1);
		}
		LocalDate localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
		for (int i = 0; i < months; i++) {
			localDate = localDate.plusMonths(1);
			String format = timeFormatter.format(localDate);
			objects.add(format);
		}


		return objects;

	}


}
