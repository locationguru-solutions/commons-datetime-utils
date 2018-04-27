package com.locationguru.csf.datetime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.Locale;

public class DateFormats
{
	private static final Logger logger = LogManager.getLogger(DateFormats.class);

	public static final DateFormatter MY_DAY_FORMATTER = new CustomFormatter("E");
	public static final DateFormatter DATE_TIME_WITH_TIMEZONE = new CustomFormatter("E");

	public enum StandardDateFormatter
			implements DateFormatter
	{
		DATE_TIME_WITH_TIMEZONE("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
		DATE_TIME("yyyy-MM-dd HH:mm:ss"),
		ISO_DATE("yyyy-MM-dd"),
		ISO_TIME("HH:mm:ss");

		private final String pattern;

		StandardDateFormatter(final String pattern)
		{
			this.pattern = pattern;
		}

		public String format(final Date date, final Locale locale)
		{
			return DateFormatUtils.formatCached(date, pattern, locale);
		}

		public String format(final Date date)
		{
			return DateFormatUtils.formatCached(date, pattern, Locale.getDefault());
		}

		@Override
		public String getPattern()
		{
			return pattern;
		}
	}

}
