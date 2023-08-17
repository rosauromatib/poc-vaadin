package com.aat.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;


/**
 * Entity implementation class for Entity: ZJTResourceCategory
 *
 */
@Entity
@Table(name="zjt_resourcecategory")
public class ZJTResourceCategory implements ZJTPo {

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column (name="zjt_resourcecategory_id")
	private int zjt_resourcecategory_id;
	
	@Column
	@NotEmpty
	private String name;
	
	@Column
	private String description;

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
