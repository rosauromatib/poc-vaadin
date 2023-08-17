package com.aat.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Entity implementation class for Entity: ZJTPricingType
 *
 */
@Entity
@Table(name="zjt_pricingtype")
public class ZJTPricingType implements ZJTPo {

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column (name="zjt_pricingtype_id")
	private int zjt_pricingtype_id;
	
	@Column
	private String name;
	
	@Column
	private String description;

	public int getZjt_pricingtype_id() {
		return zjt_pricingtype_id;
	}

	public void setZjt_pricingtype_id(int zjt_pricingtype_id) {
		this.zjt_pricingtype_id = zjt_pricingtype_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
   
}
