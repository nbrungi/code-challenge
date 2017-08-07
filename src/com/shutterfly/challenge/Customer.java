package com.shutterfly.challenge;

import java.util.Date;
import java.util.HashSet;

import com.shutterfly.challenge.Image;
import com.shutterfly.challenge.Order;
import com.shutterfly.challenge.SiteVisit;

public class Customer {

	private String id;
	private String last_name;
	private String adr_city;
	private String adr_state;
	private Date created_date;
	private Date updated_date;
	private HashSet<Order> orders;
	private HashSet<SiteVisit> site_visits;
	private HashSet<Image> images;
	
	/**
	 * @param id
	 * @param last_name
	 * @param adr_city
	 * @param adr_state
	 * @param created_date
	 * @param updated_date
	 */
	public Customer(String id, String last_name, String adr_city, String adr_state, Date created_date,
			Date updated_date) {
		super();
		this.id = id;
		this.last_name = last_name;
		this.adr_city = adr_city;
		this.adr_state = adr_state;
		this.created_date = created_date;
		this.updated_date = updated_date;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the last_name
	 */
	public String getLastName() {
		return last_name;
	}
	/**
	 * @param last_name the last_name to set
	 */
	public void setLastName(String last_name) {
		this.last_name = last_name;
	}
	/**
	 * @return the adr_city
	 */
	public String getAdrCity() {
		return adr_city;
	}
	/**
	 * @param adr_city the adr_city to set
	 */
	public void setAdrCity(String adr_city) {
		this.adr_city = adr_city;
	}
	/**
	 * @return the adr_state
	 */
	public String getAdrState() {
		return adr_state;
	}
	/**
	 * @param adr_state the adr_state to set
	 */
	public void setAdrState(String adr_state) {
		this.adr_state = adr_state;
	}
	/**
	 * @return the created_time
	 */
	public Date getCreatedDate() {
		return created_date;
	}
	/**
	 * @param created_time the created_time to set
	 */
	public void setCreatedDate(Date created_date) {
		this.created_date = created_date;
	}
	/**
	 * @return the updated_time
	 */
	public Date getUpdatedDate() {
		return updated_date;
	}
	/**
	 * @param updated_time the updated_time to set
	 */
	public void setUpdatedDate(Date updated_date) {
		this.updated_date = updated_date;
	}

	// returns orders
	public HashSet<Order> getOrders() {
		return this.orders;
	}

	// returns site visits
	public HashSet<SiteVisit> getSiteVisits() {
		return this.site_visits;
	}

	// returns images
	public HashSet<Image> getImages() {
		return this.images;
	}
	
	// sets orders of the customer
	public void setOrders(HashSet<Order> orders) {
		this.orders = orders;
	}

	// sets site visits
	public void setSiteVisits(HashSet<SiteVisit> site_visits) {
		this.site_visits = site_visits;
	}

	// sets images
	public void setImages(HashSet<Image> images) {
		this.images = images;
	}

}
