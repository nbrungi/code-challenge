package com.shutterfly.challenge;

import java.util.Date;

/**
 * Weekly Transactions logic
 */
public class WeeklyTransaction {
	private String customer_id;
	private Double expenditure = 0.0;
	private int no_of_visits = 0;
	private int week;
	private Date week_start;
	private Date week_end;

	// Creates a WeeklyTransaction with start and end date
	public WeeklyTransaction(String customer_id, int week, Date week_start,
			Date week_end) {
		this.customer_id = customer_id;
		this.week = week;
		this.week_start = week_start;
		this.week_end = week_end;
	}

	// Creates a WeeklyTransaction with start date, end date and expenditure
	public WeeklyTransaction(String customer_id, int week, Date week_start,
			Date week_end, Double expenditure) {
		this.customer_id = customer_id;
		this.week = week;
		this.week_start = week_start;
		this.week_end = week_end;
		this.expenditure = expenditure;
		this.no_of_visits = 0;
	}

	// Creates a WeeklyTransaction with start, end date, number of visits
	public WeeklyTransaction(String customer_id, int week, Date week_start,
			Date week_end, int no_of_visits) {
		this.customer_id = customer_id;
		this.week = week;
		this.week_start = week_start;
		this.week_end = week_end;
		this.expenditure = 0.0;
		this.no_of_visits = no_of_visits;
	}

	// sets week
	public void setWeek(int week) {
		this.week = week;
	}

	// sets week start date
	public void setWeekStart(Date week_start) {
		this.week_start = week_start;
	}

	// sets week end date
	public void setWeekEnd(Date week_end) {
		this.week_start = week_end;
	}

	// adds number of visits
	public void addVisit() {
		this.no_of_visits++;
	}

	// adds expenditure
	public void addExpenditure(Double expenditure) {
		this.expenditure += expenditure;
	}

	// returns week
	public int getWeek() {
		return this.week;
	}

	// returns week start date
	public Date getWeekStart() {
		return this.week_start;
	}

	// returns week end date
	public Date getWeekEnd() {
		return this.week_start;
	}

	// returns number of visits of current week for this customer
	public int getNoOfVisits() {
		return this.no_of_visits;
	}

	// returns expenditure of current week for this customer
	public Double getExpenditure() {
		return this.expenditure;
	}

	// returns expenditure per visit for current week for this customer
	public Double getExpenditurePerVisit() {
		if (this.no_of_visits == 0)
			return this.expenditure;
		else
			return (this.expenditure / Double.valueOf(this.no_of_visits));
	}
}
