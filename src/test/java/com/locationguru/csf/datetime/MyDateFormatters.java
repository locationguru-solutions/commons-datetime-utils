package com.locationguru.csf.datetime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.nio.cs.ISO_8859_2;

public class MyDateFormatters
{
	private static final Logger logger = LogManager.getLogger(MyDateFormatters.class);

	public static final DateFormatter MY_DAY_FORMATTER = new CustomFormatter("E");

}
