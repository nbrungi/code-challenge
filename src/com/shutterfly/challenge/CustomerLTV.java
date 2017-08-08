package com.shutterfly.challenge;
import java.io.File;
/*
 * main function which calls the ingest method to ingest the data and 
 * TopXSimpleLTVCustomers method to calculate the Lifetime values of the customers and 
 * writes the top n customers to the output file
 * * ******** External jar used: json-simple-1.1.1 *********
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.shutterfly.challenge.Customer;
import com.shutterfly.challenge.CustomerValue;
import com.shutterfly.challenge.WeeklyTransaction;

public class CustomerLTV {
	
	private int LIFE_SPAN = 10;
	private static List<CustomerValue> customerValues = new ArrayList<CustomerValue>();
	int no_of_weeks = 0;
	HashMap<String, Customer> customers = null;
	
	public static void main(String[] args) throws FileNotFoundException {
		try {
		CustomerLTV cltv = new CustomerLTV();
		// events are provided in input file which are ingested into data
		Object data = cltv.ingest("/input/input.txt");
		final String dir = System.getProperty("user.dir");
		String out_dir = dir + "/output/";
		// Call TopXSimpleLTVCustomers to get top N customers with highest LTV
		cltv.TopXSimpleLTVCustomers(3, data, out_dir);
		System.out.println("Check the output folder for viewing customer LTV data");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ingest method takes the input file with the list of events,
	 * returns the weekly transactions of each customer with required business metrics.
	 */
	HashMap<String, HashMap<Integer, WeeklyTransaction>> ingest(String filename)
			throws ParseException {
		EventReader eventreader = new EventReader();
		eventreader.ingest_file(filename);
		no_of_weeks = eventreader.getNoOfWeeks();
		customers = eventreader.getCustomers();
		return eventreader.getCustomersTransactions();
	}

	/* summarizeAvgWeeklyExp calculates metrics like total number of visits per week,
	 * expenditures per visit per week and the lifetime values of each customer.
	 * The result is stored in-memory which will be used to get LTV.
	 */
	void summarizeAvgWeeklyExp(String customer_id,
			HashMap<Integer, WeeklyTransaction> weeklyTransactions) {
		int no_of_visits_per_week = 0;
		Double revenue_per_visit = 0.0;
		BigDecimal total_no_of_visits = BigDecimal.ZERO;
		BigDecimal total_weekly_rev_per_visit = BigDecimal.ZERO;
		BigDecimal total_exp = BigDecimal.ZERO;
		Iterator transactionIterator = weeklyTransactions.entrySet().iterator();
		while (transactionIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) transactionIterator.next();
			no_of_visits_per_week = ((WeeklyTransaction) pair.getValue())
					.getNoOfVisits();
			revenue_per_visit = ((WeeklyTransaction) pair.getValue())
					.getExpenditurePerVisit();
			total_exp = total_exp.add(new BigDecimal(
					((WeeklyTransaction) pair.getValue()).getExpenditure()));
			BigDecimal bd_no_of_visits_per_week = new BigDecimal(
					no_of_visits_per_week, MathContext.DECIMAL64);
			total_no_of_visits = total_no_of_visits
					.add(bd_no_of_visits_per_week);
			BigDecimal bd_rev_per_visit = new BigDecimal(revenue_per_visit,
					MathContext.DECIMAL64);
			total_weekly_rev_per_visit = total_weekly_rev_per_visit
					.add(bd_rev_per_visit);
		}
		CustomerValue customerValue = new CustomerValue(
				customers.get(customer_id), total_no_of_visits, LIFE_SPAN,
				total_weekly_rev_per_visit, total_exp, no_of_weeks);
		customerValues.add(customerValue);
	}
	
	/*
	 * TopXSimpleLTVCustomers takes the number of customers to be returned,
	 * input data (weekly transactions of the customer) and the directory where the output file 
	 * will be placed, ranks the customers with top N high lifetime values
	 * and writes it to a csv file
	 * 
	 * The output filename with customer details will be in the below format in /output folder:
	 * top_n_LTV_yyyyMMddHHmmss.csv
	 */
	void TopXSimpleLTVCustomers(int n, Object obj, String out_dir)
			throws ParseException, FileNotFoundException {
		HashMap<String, HashMap<Integer, WeeklyTransaction>> customersTrans = (HashMap<String, HashMap<Integer, WeeklyTransaction>>) obj;

		Iterator transactionIterator = customersTrans.entrySet()
				.iterator();
		while (transactionIterator.hasNext()) {
			Map.Entry pair = (Map.Entry) transactionIterator.next();
			String customer_id = (String) pair.getKey();
			HashMap<Integer, WeeklyTransaction> weeklyTransactions = (HashMap<Integer, WeeklyTransaction>) pair
					.getValue();
			summarizeAvgWeeklyExp(customer_id, weeklyTransactions);
		}
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date today = Calendar.getInstance().getTime();
		String formatDate = sdf.format(today);
		PrintWriter pw = new PrintWriter(new File(out_dir + "top_" + n
				+ "_LTV_" + formatDate + "test.csv"));
		pw.write("customer_id,last_name,state,lifetime_value\n");

		Collections.sort(customerValues, new Comparator<CustomerValue>() {
			public int compare(CustomerValue o1, CustomerValue o2) {
				return o1.getLTV().compareTo(o2.getLTV());
			}
		});
		Collections.reverse(customerValues);

		if (n > customerValues.size())
			n = customerValues.size();
		for (int i = 0; i < n; i++) {
			CustomerValue ltv = customerValues.get(i);
			sb.append('"');
			sb.append(ltv.getCustomer().getId());
			sb.append('"');
			sb.append(',');
			sb.append('"');
			sb.append(ltv.getCustomer().getLastName());
			sb.append('"');
			sb.append(',');
			sb.append('"');
			sb.append(ltv.getCustomer().getAdrState());
			sb.append('"');
			sb.append(',');
			sb.append(ltv.getLTV());
			sb.append('\n');
		}
		pw.write(sb.toString());
		pw.close();
	}
}
