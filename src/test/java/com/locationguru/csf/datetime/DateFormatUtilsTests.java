package com.locationguru.csf.datetime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.locationguru.csf.datetime.DateFormats.StandardDateFormatter.ISO_DATE;
import static com.locationguru.csf.datetime.MyDateFormatters.MY_DAY_FORMATTER;

public class DateFormatUtilsTests
{
	private static final Logger logger = LogManager.getLogger(DateFormatUtilsTests.class);

	private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);

	@Test
	public void testDateFormatting()
	{
		final Date date = Timestamp.valueOf("2018-04-30 12:00:00");
		final String format = DateFormatUtils.format(date, "dd-MM-yyyy");

		Assertions.assertEquals("30-04-2018", format);
	}

	@Test
	public void testDateFormattingWithNullLocale()
	{
		final Date date = Timestamp.valueOf("2018-04-30 12:00:00");

		Assertions.assertThrows(NullPointerException.class, () -> DateFormatUtils.format(null, "dd-MM-yyyy", Locale.getDefault()));
		Assertions.assertThrows(NullPointerException.class, () -> DateFormatUtils.format(date, null, Locale.getDefault()));
		Assertions.assertThrows(NullPointerException.class, () -> DateFormatUtils.format(date, "dd-MM-yyyy", null));
	}

	@Test
	public void testDateFormattingWithGermanLocale()
	{
		final Date date = Timestamp.valueOf("2018-04-30 12:00:00");
		final String format = DateFormatUtils.format(date, "E", Locale.GERMANY);

		Assertions.assertEquals("Mo", format);
	}

	@Test
	public void testDateFormattingWithUsLocale()
	{
		final Date date = Timestamp.valueOf("2018-04-30 12:00:00");
		final String format = DateFormatUtils.format(date, "E", Locale.US);

		Assertions.assertEquals("Mon", format);
	}

	@Test
	public void testConsistentDateFormatting()
	{
		final Timestamp timestamp = Timestamp.valueOf("2018-04-30 12:00:00");
		final Date date = new Date(timestamp.getTime());

		final String formattedDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		final String formattedTimestamp = DateFormatUtils.format(timestamp, "yyyy-MM-dd");

		Assertions.assertEquals("2018-04-30", formattedDate);
		Assertions.assertEquals(formattedDate, formattedTimestamp);
	}

	@Test
	public void testNonCachedFormatterPerformance()
	{
		final Timestamp timestamp = Timestamp.valueOf("2018-04-30 12:00:00");

		long time = System.currentTimeMillis();

		for (int i = 0; i < 10_000; i++)
		{
			final String formattedTimestamp = DateFormatUtils.format(timestamp, "yyyy-MM-dd", Locale.US);
		}

		final long runtime = System.currentTimeMillis() - time;
		logger.info("{}ms for 10_000 non-cached formatting cycles ..", runtime);
	}

	@Test
	public void testCachedFormatterPerformance()
	{
		final Timestamp timestamp = Timestamp.valueOf("2018-04-30 12:00:00");

		long time = System.currentTimeMillis();

		for (int i = 0; i < 10_000; i++)
		{
			final String formattedTimestamp = DateFormatUtils.formatCached(timestamp, "yyyy-MM-dd", Locale.US);
		}

		final long runtime = System.currentTimeMillis() - time;
		logger.info("{}ms for 10_000 cached formatting cycles ..", runtime);
	}

	@Test
	public void testNonCachedFormatterMultiThreadedPerformance() throws ExecutionException, InterruptedException
	{
		final Timestamp timestamp = Timestamp.valueOf("2018-04-30 12:00:00");
		final List<Future<String>> futures = new ArrayList<>();
		long time = System.currentTimeMillis();

		for (int i = 0; i < 10_000; i++)
		{
			futures.add(executorService.submit(() -> DateFormatUtils.format(timestamp, "yyyy-MM-dd", Locale.US)));
		}

		for (final Future<String> future : futures)
		{
			future.get();
		}

		final long runtime = System.currentTimeMillis() - time;
		logger.info("{}ms for 10_000 non-cached multi-threaded formatting cycles ..", runtime);
	}

	@Test
	public void testCachedFormatterMultiThreadedPerformance() throws ExecutionException, InterruptedException
	{
		final Timestamp timestamp = Timestamp.valueOf("2018-04-30 12:00:00");
		final List<Future<String>> futures = new ArrayList<>();
		long time = System.currentTimeMillis();

		for (int i = 0; i < 10_000; i++)
		{
			futures.add(executorService.submit(() -> DateFormatUtils.formatCached(timestamp, "yyyy-MM-dd", Locale.US)));
		}

		for (final Future<String> future : futures)
		{
			future.get();
		}

		final long runtime = System.currentTimeMillis() - time;
		logger.info("{}ms for 10_000 cached multi-threaded formatting cycles ..", runtime);
	}

	public void testFormatterWithEnums()
	{
		ISO_DATE.format(new Timestamp(System.currentTimeMillis()));
		MY_DAY_FORMATTER.format(new Date(System.currentTimeMillis()));
	}

	@AfterAll
	public static void destroy()
	{
		executorService.shutdownNow();
	}
}
