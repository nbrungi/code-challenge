package com.shutterfly.challenge;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import org.json.simple.JSONObject;

	/**
	 *         The EventComparator Class implements Comparator
	 *         interface to compare two JSONObjects by event time and types.
	 * 
	 *         This comparator is used to sort the Events stored in JSONArray by
	 *         their type and event_time.
	 */
	public class EventComparator implements Comparator<JSONObject>{

		@Override
		public int compare(JSONObject o1, JSONObject o2) {
			Date event_time_o1 = null;
			Date event_time_o2 = null;
			String type_o1 = (String)o1.get("type");
			String type_o2 = (String)o2.get("type");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
			try {
			    event_time_o1 = simpleDateFormat.parse((String)o1.get("event_time"));
				event_time_o2 = simpleDateFormat.parse((String)o2.get("event_time"));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			int result = type_o1.compareTo(type_o2);
			if (result==0) 
				result = event_time_o1.compareTo(event_time_o2);
			
			return result;
		} 
		
	}

