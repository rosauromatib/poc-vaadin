package com.aat.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="zjt_resourcetype")
public class ZJTResourceType implements ZJTPo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column (name="zjt_resourcetype_id")
	private int zjt_resourcetype_id;
	
	@Column
	@NotEmpty
	private String name;

	@Column
	private String description;

	@ManyToOne
	@JoinColumn(name="zjt_resourcecategory_id")
	@NotNull
	private ZJTResourceCategory resourceCategory;

//	@Transient
//	@Column
//	private String cname;
	
	public int getZjt_resourcetype_id() {
		return zjt_resourcetype_id;
	}

	public void setZjt_resourcetype_id(int zjt_resourcetype_id) {
		this.zjt_resourcetype_id = zjt_resourcetype_id;
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

	public ZJTResourceCategory getResourceCategory() {
		return resourceCategory;
	}

	public void setResourceCategory(ZJTResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
	}

//	public String getCname() {
//		return cname;
////		return resourceCategory.getName();
//	}
//
//	public void setCname(String cname) {
//		this.cname = cname;
//	}

	


	
}
