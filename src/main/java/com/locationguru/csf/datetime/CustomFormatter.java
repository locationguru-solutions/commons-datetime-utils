package com.locationguru.csf.datetime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.Locale;

public class CustomFormatter
		implements DateFormatter
{
	private static final Logger logger = LogManager.getLogger(CustomFormatter.class);

	private final String pattern;

	public CustomFormatter(final String pattern)
	{
		this.pattern = pattern;
	}

	public String getPattern()
	{
		return pattern;
	}
}
