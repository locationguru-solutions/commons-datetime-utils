package com.locationguru.csf.datetime;

import java.util.Date;
import java.util.Locale;

public interface DateFormatter
{
	public String getPattern();

	public default String format(final Date date, final Locale locale)
	{
		return DateFormatUtils.formatCached(date, getPattern(), locale);
	}

	public default String format(final Date date)
	{
		return DateFormatUtils.formatCached(date, getPattern(), Locale.getDefault());
	}
}
