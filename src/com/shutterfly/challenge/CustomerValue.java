package com.shutterfly.challenge;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *         LifeTimeValue consolidates the customer's number of visits/week,
 *         expenditures/visit, total expenditure and life span
 */

public class CustomerValue {
	private Customer customer;
	private BigDecimal total_visits_per_week;
	private BigDecimal total_expenditures_per_visit;
	private BigDecimal total_expenditure;
	private int life_span;
	private BigDecimal life_time_value;
	private int no_of_weeks;

	/*
	 * creates a LifeTimeValue
	 * 
	 */
	public CustomerValue(Customer customer, BigDecimal no_of_visits_per_week,
			int life_span, BigDecimal expenditures_per_visit,
			BigDecimal total_exp, int no_of_weeks) {
		this.customer = customer;
		this.total_visits_per_week = no_of_visits_per_week;
		this.life_span = life_span;
		this.total_expenditures_per_visit = expenditures_per_visit;
		this.no_of_weeks = no_of_weeks;
		this.total_expenditure = total_exp;
		calculateLTV();
	}

	/*
	 * calculates the lifetime values LifeTime Value = 52 * (customer
	 * expenditures per visit (USD) * number of site visits per week) * lifespan
	 */
	void calculateLTV() {
		this.life_time_value = (total_visits_per_week
				.divide(new BigDecimal(this.no_of_weeks), 5, RoundingMode.HALF_UP)
				.multiply(total_expenditures_per_visit)
				.divide(new BigDecimal(this.no_of_weeks), 5, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(this.no_of_weeks))
				.multiply(new BigDecimal(52 * life_span)));
	}

	/* sets number of visits and calculates LifeTime Value */
	public void setNoOfVisits(BigDecimal no_of_visits_per_week) {
		this.total_visits_per_week = no_of_visits_per_week;
		calculateLTV();
	}

	/* sets weekly expenditure and calculates LifeTime Value */
	public void setTotalWeeklyExp(BigDecimal expenditures_per_visit) {
		this.total_expenditures_per_visit = expenditures_per_visit;
		calculateLTV();
	}

	// sets life span
	public void setLifeSpan(int life_span) {
		this.life_span = life_span;
		calculateLTV();
	}

	// returns number of visits
	public BigDecimal getNoOfVisits() {
		return this.total_visits_per_week;
	}

	// returns number of expenditure per visit
	public BigDecimal getExpdPerVisit() {
		return this.total_expenditures_per_visit;
	}

	// returns lifetime value
	public BigDecimal getLTV() {
		return this.life_time_value;
	}

	// returns life span
	public int getLifeSpan() {
		return this.life_span;
	}

	// returns total expenditure
	public BigDecimal getTotalExpenditure() {
		return this.total_expenditure;
	}

	// returns customer
	public Customer getCustomer() {
		return this.customer;
	}
}
