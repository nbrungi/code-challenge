package com.shutterfly.challenge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.HashSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *        Class to parse the Event to corresponding object:
 *         Customer, Image, Site Visit or Order
 */
public class EventParser {
	private static Date timeframe_start = null;
	private static Date timeframe_end = null;
	private static HashMap<String, Customer> customers = new HashMap<String, Customer>();
	private static HashMap<String, HashSet<SiteVisit>> site_visits = 
			new HashMap<String, HashSet<SiteVisit>>();
	private static HashMap<String, HashSet<Order>> orders = new HashMap<String, HashSet<Order>>();
	private static HashMap<String, HashSet<Image>> images = new HashMap<String, HashSet<Image>>();

	/*
	 * parseEvents gets the JSONArray, iterates every JSONObject and 
	 * 
	 */
	void parseEvents(JSONArray events) throws ParseException {
		Iterator<?> eventsIterator = events.iterator();
		eventsIterator = events.iterator();
		while (eventsIterator.hasNext()) {
			JSONObject event = (JSONObject) eventsIterator.next();
			String event_type = (String) event.get("type");
			if (event_type != null) {
				switch (event_type) {
				case "CUSTOMER":
					parseCustomer(event);
					break;
				case "SITE_VISIT":
					parseSiteVisit(event);
					break;
				case "IMAGE":
					parseImage(event);
					break;
				case "ORDER":
					parseOrder(event);
					break;
				}
			}
		}
	}

	// ******NOTE: Need to generalize following functions. an interface?
	/*
	 * parseCustomer parses JSONObject of a Customer Event. It parses each field
	 * to Customer object and stores the customer object in list of customers.
	 */
	private void parseCustomer(JSONObject customerEvent) throws ParseException {
		Customer customer = null;
		String verb = (String) customerEvent.get("verb");
		String customer_id = (String) customerEvent.get("key");
		String last_name = (String) customerEvent.get("last_name");
		String city = (String) customerEvent.get("adr_city");
		String state = (String) customerEvent.get("adr_state");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		Date date = dateFormat.parse((String) customerEvent.get("event_time"));
		// If the Event type is new, it creates a new Customer
		if ("NEW".equalsIgnoreCase(verb) && customer_id != null
				&& !customers.containsKey("customer_id")) {
			customer = new Customer(customer_id, last_name, city, state, date, date);
			// Else if type is update, it updates the eexisting customer object
		} else if ("UPDATE".equalsIgnoreCase(verb) && customer_id != null
				&& customers.containsKey("customer_id")) {
			customer = customers.get("customer_id");
			customer.setAdrCity(city);
			customer.setLastName(last_name);
			customer.setAdrState(state);
			customer.setUpdatedDate(date);
		}
		if (customer_id != null)
			customers.put(customer_id, customer);
	}

	/*
	 * parseSiteVisit parses JSONObject of a SiteVisit Event. It parses each field
	 * to SiteVisit object and stores the sitevisit object in list of sitevisits.
	 */
	private void parseSiteVisit(JSONObject siteVisitEvent)
			throws ParseException {
		SiteVisit site_visit = null;
		String verb = (String) siteVisitEvent.get("verb");
		String site_visit_id = (String) siteVisitEvent.get("key");
		String customer_id = (String) siteVisitEvent.get("customer_id");
		JSONArray tagsJson = (JSONArray) siteVisitEvent.get("tags");
		HashMap<String, String> tags = new HashMap<String, String>();
		Iterator<?> it = tagsJson.iterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();
			Iterator pair = jsonObject.entrySet().iterator();
			Entry entry = null;
			while (pair.hasNext()) {
				entry = (Entry) pair.next();
				tags.put((String) entry.getKey(), (String) entry.getValue());
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		Date date = dateFormat.parse((String) siteVisitEvent.get("event_time"));
		check_and_setTimeFrame(date);
		// If the Event type is new, it creates a new SiteVisit
		if ("NEW".equalsIgnoreCase(verb) && site_visit_id != null) {
			site_visit = new SiteVisit(site_visit_id, customer_id, tags, date);
		}
		// Else if type is update, it updates the existing Site_Visit object
		if (customer_id != null && site_visits.containsKey(customer_id)) {
			site_visits.get(customer_id).add(site_visit);
		} else {
			HashSet<SiteVisit> customer_site_visits = new HashSet<SiteVisit>();
			customer_site_visits.add(site_visit);
			site_visits.put(customer_id, customer_site_visits);
		}
	}

	/*
	 * parseSiteVisit parses JSONObject of a Order Event. It parses each field
	 * to Order object and stores the order object in list of orders.
	 */
	private void parseOrder(JSONObject orderEvent) throws ParseException {
		Order order = null;
		String verb = (String) orderEvent.get("verb");
		String order_id = (String) orderEvent.get("key");
		String customer_id = (String) orderEvent.get("customer_id");
		Double total_amount = 0.0;
		String currency = null;
		String total_amount_str = (String) orderEvent.get("total_amount");
		if (total_amount_str != null) {
			total_amount = Double.valueOf(
					total_amount_str.replaceAll("[^\\.0123456789]", ""));
			currency = total_amount_str.replaceAll("[^A-Za-z]+", "");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		Date date = dateFormat.parse((String) orderEvent.get("event_time"));
		check_and_setTimeFrame(date);
		// If the Event type is new, it creates a new Order
		if ("NEW".equalsIgnoreCase(verb) && order_id != null) {
			order = new Order(order_id, customer_id, total_amount, currency,
					date, date);
			// Else if type is update, it updates the existing order object
		} else if ("UPDATE".equalsIgnoreCase(verb) && order_id != null) {
			Order updated_order = new Order(order_id, customer_id, total_amount,
					currency, date, date);
			Iterator<Order> orderIterator = orders.get(customer_id).iterator();
			Order old_ord = null;
			while (orderIterator.hasNext()) {
				old_ord = orderIterator.next();
				if (old_ord.equals(updated_order)) {
					updated_order.setCreatedDate(old_ord.getCreatedDate(),date);
					break;
				}
			}
			if (old_ord != null)
				orders.get(customer_id).remove(old_ord);
		}
		if (customer_id != null && orders.containsKey(customer_id)) {
			orders.get(customer_id).add(order);
		} else {
			HashSet<Order> customer_orders = new HashSet<Order>();
			customer_orders.add(order);
			orders.put(customer_id, customer_orders);
		}
	}

	/*
	 * parseOrder parses JSONObject of a Image Event. It parses each field
	 * to Image object and stores the image object in list of images.
	 */
	private void parseImage(JSONObject imageEvent) throws ParseException {
		Image image = null;
		String verb = (String) imageEvent.get("verb");
		String image_id = (String) imageEvent.get("key");
		String customer_id = (String) imageEvent.get("customer_id");
		String camera_make = (String) imageEvent.get("camera_make");
		String camera_model = (String) imageEvent.get("camera_model");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		Date date = dateFormat.parse((String) imageEvent.get("event_time"));
		// if event type of image is upload, creates a new Image
		if ("UPLOAD".equalsIgnoreCase(verb) && image_id != null) {
			image = new Image(customer_id, camera_make, camera_model,
					date, date, image_id);
		}
		if (customer_id != null && images.containsKey(customer_id)) {
			images.get(customer_id).add(image);
		} else {
			HashSet<Image> customer_images = new HashSet<Image>();
			customer_images.add(image);
			images.put(customer_id, customer_images);
		}
	}

	/* check_and_setTimeFrame is used to set the upper bound and lower bound of
	* timeframe. it based on the lowest and highest event time of orders and site 
	* visits
	*/
	void check_and_setTimeFrame(Date date) {
		if (timeframe_start == null)
			timeframe_start = date;
		else if (timeframe_start.after(date))
			timeframe_start = date;

		if (timeframe_end == null)
			timeframe_end = date;
		else if (timeframe_end.before(date))
			timeframe_end = date;
	}

	// returns timeframe start time
	public Date getStartDateTime() {
		return timeframe_start;
	}

	// returns timeframe end time
	public Date getEndDateTime() {
		return timeframe_end;
	}

	// returns hashmap which consists the customers with customer id as key
	public HashMap<String, Customer> getCustomers() {
		return customers;
	}
	// returns hashmap which consists the site visits with customer id as key
	public HashMap<String, HashSet<SiteVisit>> getSiteVisits() {
		return site_visits;
	}

	// returns hashmap which consists the orders with customer id as key
	public HashMap<String, HashSet<Order>> getOrders() {
		return orders;
	}

	public HashMap<String, HashSet<Image>> getImages() {
		return images;
	}
}

