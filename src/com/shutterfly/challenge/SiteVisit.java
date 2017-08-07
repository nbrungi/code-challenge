package com.shutterfly.challenge;

import java.util.Date;
import java.util.HashMap;

/**
 * 		   SiteVisit represents a site visit event of a customer. If
 *         the action of the event is NEW new object will be created. The object
 *         will be updated if the action is UPDATE.
 * 
 */
public class SiteVisit {
	private String id;
	private String customer_id;
	private HashMap<String, String> tags;
	private Date createdDateTime;
	private Date updatedDateTime;

	// Creates new site visit
	public SiteVisit(String id, String customer_id,
			HashMap<String, String> tags, Date date) {
		this.id = id;
		this.customer_id = customer_id;
		this.tags = tags;
		this.createdDateTime = date;
		this.updatedDateTime = date;
	}

	// sets customer id
	public void setCustomerId(String customer_id) {
		this.customer_id = customer_id;
	}

	// sets tags
	public void setTags(HashMap<String, String> tags) {
		this.tags = tags;
	}

	// sets created date
	public void setCreatedDate(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = createdDateTime;
	}

	// returns site visit id
	public String getId() {
		return this.id;
	}

	// returns customer id
	public String getCustomerId() {
		return this.customer_id;
	}

	// returns tags
	public HashMap<String, String> getTags() {
		return this.tags;
	}

	// returns created date
	public Date getCreatedDate() {
		return this.createdDateTime;
	}

	// returns updated date
	public Date getUpdatedDate() {
		return this.updatedDateTime;
	}

	// returns hashcode of id and customer id
	@Override
	public int hashCode() {
		return (id + customer_id).hashCode();
	}

	// compares site visit
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SiteVisit) {
			SiteVisit sitevisit = (SiteVisit) obj;
			return (sitevisit.id.equals(this.id)
					&& sitevisit.customer_id.equals(this.customer_id));
		} else {
			return false;
		}
	}

}
