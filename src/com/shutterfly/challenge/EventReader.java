package com.shutterfly.challenge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author gkuppuswamy EventReader Class implements the ingest method which
 *         loads the events into im-memory data structure which can be later
 *         used to calculate LifeTime Value of each customer.
 * 
 */
public class EventReader {
	// start date of time frame
	static Date timeframe_start = null;
	// stop date of time frame
	static Date timeframe_end = null;
	static HashMap<String, HashMap<Integer, WeeklyTransaction>> customerTransactions = new HashMap<String, HashMap<Integer, WeeklyTransaction>>();
	static NavigableMap<Date, Integer> ranges = null;
	static int no_of_weeks = 0;
	static HashMap<String, Customer> customers = null;

	/*
	 * ingest_file reads a file, parses the data from JSON to data structures
	 * and summarizes the data based on each week for the time frame of data
	 */
	void ingest_file(String file) throws java.text.ParseException {
		EventReader eventreader = new EventReader();
		final String dir = System.getProperty("user.dir");
		HashMap<String, Customer> data = eventreader
				.readEvents(new File(dir + file));
		Iterator dataIterator = data.entrySet().iterator();
		while (dataIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) dataIterator.next();
			String customer_id = (String) pair.getKey();
			HashSet<SiteVisit> sitevisits = ((Customer) pair.getValue())
					.getSiteVisits();
			if (sitevisits!=null)
				eventreader.summarizeWeeklyCustomerSiteVisits(customer_id,
					sitevisits);
			
			HashSet<Order> orders = ((Customer) pair.getValue()).getOrders();
			if (orders!=null)
				eventreader.summarizeWeeklyCustomerOrders(customer_id, orders);
		}
	}

	/*
	 * readEvents ingests the input file, convert the JSON contents to
	 * JSONObject and parse it to relevant objects and stores them. It returns
	 * the dataset to customer details with orders, sitevisits and images.
	 */
	HashMap<String, Customer> readEvents(File input_file)
			throws java.text.ParseException {
		
		JSONParser jsonParser = new JSONParser();
		try {
			JSONArray events = (JSONArray) jsonParser
					.parse(new FileReader(input_file));
			// sorts the events by type and time. this ensures that the data is
			// chronological
			events.sort(new EventComparator());
			EventParser eventParser = new EventParser();
			eventParser.parseEvents(events);
			timeframe_start = eventParser.getStartDateTime();
			timeframe_end = eventParser.getEndDateTime();
			// parse customers
			customers = eventParser.getCustomers();
			// parse site visits
			HashMap<String, HashSet<SiteVisit>> sitevisits = eventParser
					.getSiteVisits();
			// parse orders
			HashMap<String, HashSet<Order>> orders = eventParser.getOrders();
			// parse images
			HashMap<String, HashSet<Image>> images = eventParser.getImages();
			TimeRange timerange = new TimeRange();
			ranges = timerange.generateWeeks(timeframe_start, timeframe_end);
			no_of_weeks = ranges.lastEntry().getValue();

			Iterator it = customers.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Customer customer_obj = (Customer) pair.getValue();
				String customer_id = (String) pair.getKey();
				customer_obj.setSiteVisits(sitevisits.get(customer_id));
				customer_obj.setOrders(orders.get(customer_id));
				customer_obj.setImages(images.get(customer_id));
			}
			return customers;
		} catch (ParseException parse_Ex) {
			parse_Ex.printStackTrace();
		} catch (FileNotFoundException fileNotFound_Ex) {
			fileNotFound_Ex.printStackTrace();
		} catch (IOException io_Ex) {
			io_Ex.printStackTrace();
		}
		return null;
	}

	/*
	 * Summarizes the visits and expenditure by week per customer
	 */
	void summarizeWeeklyCustomerSiteVisits(String customer_id,
			HashSet<SiteVisit> site_visits) {
		Iterator sitevisitIterator = site_visits.iterator();
		while (sitevisitIterator.hasNext()) {
			SiteVisit sitevisit = (SiteVisit) sitevisitIterator.next();
			// gets the week key from the NavigableMap of TimeFrame
			int week = ranges.ceilingEntry(sitevisit.getUpdatedDate())
					.getValue();
			// Gets the first day of week (SUNDAY)
			Date week_start = ranges.ceilingEntry(sitevisit.getUpdatedDate())
					.getKey();
			Calendar cal = Calendar.getInstance();
			cal.setTime(week_start);
			cal.add(Calendar.DAY_OF_YEAR, 6);
			// setting the week end date
			Date week_end = cal.getTime();
			if (customerTransactions.containsKey(customer_id)) {
				if (customerTransactions.get(customer_id).containsKey(week))
					customerTransactions.get(customer_id).get(week).addVisit(); // Test
																				// this
				else {
					WeeklyTransaction weekly_transaction = new WeeklyTransaction(
							customer_id, week, week_start, week_end, 1);
					customerTransactions.get(customer_id).put(week,
							weekly_transaction);
				}
			} else {
				WeeklyTransaction weekly_transaction = new WeeklyTransaction(
						customer_id, week, week_start, week_end, 1);
				HashMap<Integer, WeeklyTransaction> weeklytransactions = new HashMap<Integer, WeeklyTransaction>();
				weeklytransactions.put(week, weekly_transaction);
				customerTransactions.put(customer_id, weeklytransactions);
			}

		}
	}

	/*
	 * Summarizes the visits and expenditure by week per customer
	 */

	void summarizeWeeklyCustomerOrders(String customer_id,
			HashSet<Order> orders) {
		Iterator orderIterator = orders.iterator();
		while (orderIterator.hasNext()) {
			Order order = (Order) orderIterator.next();
			int week = ranges.ceilingEntry(order.getUpdatedDate()).getValue();
			// Gets the first day of week (SUNDAY)
			Date week_start = ranges.ceilingEntry(order.getUpdatedDate())
					.getKey();
			Calendar cal = Calendar.getInstance();
			cal.setTime(week_start);
			cal.add(Calendar.DAY_OF_YEAR, 6);
			// setting the week end date
			Date week_end = cal.getTime();
			if (customerTransactions.containsKey(customer_id)) {
				if (customerTransactions.get(customer_id).containsKey(week))
					customerTransactions.get(customer_id).get(week)
							.addExpenditure(order.getTotalAmount()); // Test
				// this
				else {
					WeeklyTransaction weekly_transaction = new WeeklyTransaction(
							customer_id, week, week_start, week_end,
							order.getTotalAmount());
					customerTransactions.get(customer_id).put(week,
							weekly_transaction);
				}
			} else {
				WeeklyTransaction weekly_transaction = new WeeklyTransaction(
						customer_id, week, week_start, week_end,
						order.getTotalAmount());
				HashMap<Integer, WeeklyTransaction> weeklytransactions = new HashMap<Integer, WeeklyTransaction>();
				weeklytransactions.put(week, weekly_transaction);
				customerTransactions.put(customer_id, weeklytransactions);
			}
		}
	}

	// returns the weekly transactions - which is D used to calculate
	// top n highest lifetime value customers
	public HashMap<String, HashMap<Integer, WeeklyTransaction>> getCustomersTransactions() {
		return this.customerTransactions;
	}

	// returns number of weeks
	public int getNoOfWeeks() {
		return this.no_of_weeks;
	}

	// returns customers
	public HashMap<String, Customer> getCustomers() {
		return customers;
	}
}

