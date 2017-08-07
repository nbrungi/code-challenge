package com.shutterfly.challenge;

import java.util.Date;

public class Image {

	/**
	 * @param customer_id
	 * @param camera_make
	 * @param camera_model
	 * @param created_date
	 * @param updated_date
	 * @param id
	 */
	public Image(String customer_id, String camera_make, String camera_model, Date created_date, Date updated_date,
			String id) {
		super();
		this.customer_id = customer_id;
		this.camera_make = camera_make;
		this.camera_model = camera_model;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.id = id;
	}

	private String customer_id;
	private String camera_make;
	private String camera_model;
	private Date created_date;
	private Date updated_date;
	

		private String id;
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
		 * @return the customer_id
		 */
		public String getCustomer_id() {
			return customer_id;
		}
		/**
		 * @param customer_id the customer_id to set
		 */
		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}
		/**
		 * @return the camera_make
		 */
		public String getCamera_make() {
			return camera_make;
		}
		/**
		 * @param camera_make the camera_make to set
		 */
		public void setCamera_make(String camera_make) {
			this.camera_make = camera_make;
		}
		/**
		 * @return the camera_model
		 */
		public String getCamera_model() {
			return camera_model;
		}
		/**
		 * @param camera_model the camera_model to set
		 */
		public void setCamera_model(String camera_model) {
			this.camera_model = camera_model;
		}
		/**
		 * @return the created_date
		 */
		public Date getCreated_date() {
			return created_date;
		}
		/**
		 * @param created_date the created_date to set
		 */
		public void setCreated_date(Date created_date) {
			this.created_date = created_date;
		}
		/**
		 * @return the updated_date
		 */
		public Date getUpdated_date() {
			return updated_date;
		}
		/**
		 * @param updated_date the updated_date to set
		 */
		public void setUpdated_date(Date updated_date) {
			this.updated_date = updated_date;
		}

}
