package com.shutterfly.challenge;

import java.util.Calendar;
import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;

/*
 * TimeRange implements generateWeekRanges method to generate ranges of dates
 * with a key. This will be used to groups Orders & Site_Visits of a customer
 */
public class TimeRange {
	/*
	 * generateWeeks takes the start date and end date and returns a navigable
	 * hashmap with the lower bound of every week (SUNDAY) between start
	 * and end dates with a unique key
	 */
	NavigableMap<Date, Integer> generateWeeks(Date startDate, Date endDate) {
		if (startDate.after(endDate))
			return null;
		NavigableMap<Date, Integer> weekRanges = new TreeMap<Date, Integer>();
		int week_no = 1;
		
		Calendar timeFrameCal = Calendar.getInstance();
		timeFrameCal.setFirstDayOfWeek(Calendar.SUNDAY);
		timeFrameCal.setTime(startDate);
		timeFrameCal.set(Calendar.HOUR_OF_DAY, 0);
		timeFrameCal.set(Calendar.MINUTE, 0);
		timeFrameCal.set(Calendar.SECOND, 0);
		timeFrameCal.set(Calendar.MILLISECOND, 0);

		Calendar first = (Calendar) timeFrameCal.clone();
		first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek());
		weekRanges.put(first.getTime(), week_no);
		Calendar last = (Calendar) first.clone();
		Date last_date = last.getTime();
		while (last_date.before(endDate)) {
			week_no++;
			last.add(Calendar.DAY_OF_YEAR, 7);
			last_date = last.getTime();
			weekRanges.put(last_date, week_no);
		}

		return weekRanges;
	}
}
