package com.locationguru.csf.datetime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;

public class DateFormatUtils
{
	private static final Logger logger = LogManager.getLogger(DateFormatUtils.class);
	// private final static Map<String, DateFormat> dateFormats = Collections.synchronizedMap(new HashMap<>());

	private static final ThreadLocal<Map<String, DateFormat>> dateFormats = ThreadLocal.withInitial((Supplier<HashMap<String, DateFormat>>) HashMap::new);

	public static String format(final Date date, final String format, final Locale locale)
	{
		Objects.requireNonNull(date, "timestamp can't be null");
		Objects.requireNonNull(format, "format can't be null");
		Objects.requireNonNull(locale, "locale can't be null");

		// final String key = format + "_" + locale.getLanguage() + "_" + locale.getCountry();

		// return dateFormats.computeIfAbsent(key, cacheKey -> new SimpleDateFormat(format, locale))
		// 				  .format(date);

		return new SimpleDateFormat(format, locale).format(date);
	}

	public static String formatCached(final Date date, final String format, final Locale locale)
	{
		Objects.requireNonNull(date, "timestamp can't be null");
		Objects.requireNonNull(format, "format can't be null");
		Objects.requireNonNull(locale, "locale can't be null");

		final String key = format + "_" + locale.getLanguage() + "_" + locale.getCountry();

		return dateFormats.get()
						  .computeIfAbsent(key, cacheKey -> new SimpleDateFormat(format, locale))
						  .format(date);
	}

	public static String format(final Date date, final String format)
	{
		return format(date, format, Locale.getDefault());
	}

	public static String formatCurrentDate(final String format)
	{
		return format(new Date(System.currentTimeMillis()), format, Locale.getDefault());
	}

	public static String formatCurrentDate(final String format, final Locale locale)
	{
		return format(new Date(System.currentTimeMillis()), format, locale);
	}

}
