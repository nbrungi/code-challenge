package com.shutterfly.challenge;

import java.util.Date;

/**
 * This class represents the order placed by a customer. 
 * An Order can be created or updated by the verb - NEW or UPDATE
 * 
 */
public class Order {
	private String id;
	private String customer_id;
	private Double total_amount;
	private String currency;
	private Date created_date;
	private Date updated_date;

	// Creates new Order
	public Order(String id, String customer_id, Double total_amount,
			String currency, Date created_date, Date updated_date) {
		this.id = id;
		this.customer_id = customer_id;
		this.total_amount = total_amount;
		this.currency = currency;
		this.created_date = created_date;
		this.updated_date = updated_date;
	}

	// sets customer id
	public void setCustomerId(String customer_id) {
		this.customer_id = customer_id;
	}

	// sets total amount
	public void setTotalAmount(Double total_amount) {
		this.total_amount = total_amount;
	}

	// sets currency
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	// sets createddate
	public void setCreatedDate(Date createdDate, Date updatedDate) {
		this.created_date = createdDate;
		this.updated_date = updatedDate;
	}

	// sets get Id
	public String getId() {
		return this.id;
	}

	// returns customer id
	public String getCustomerId() {
		return this.customer_id;
	}

	// returns total amount
	public Double getTotalAmount() {
		return this.total_amount;
	}

	// returns currency
	public String getCurrency() {
		return this.currency;
	}

	// returns created date
	public Date getCreatedDate() {
		return this.created_date;
	}

	// returns updated date
	public Date getUpdatedDate() {
		return this.updated_date;
	}
	
	// returns hashcode value for customer id and id.
	@Override
	public int hashCode() {
		return (id + customer_id).hashCode();
	}

	// Compares the Orders
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Order) {
			Order order = (Order) obj;
			return (order.id.equals(this.id)
					&& order.customer_id.equals(this.customer_id));
		} else {
			return false;
		}
	}

}
