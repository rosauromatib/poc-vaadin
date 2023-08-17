package com.aat.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name="zjt_element")
public class ZJTElement implements ZJTPo {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int zjt_element_id;
	
	@Column
	@NotEmpty
	private String name;
	
	@ManyToOne
	@JoinColumn(name="zjt_pricingtype_id")
	@NotNull
	private ZJTPricingType pricingType;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private Uom uom;

	@Enumerated(EnumType.STRING)
	private ElementList elementlist;
	
	public int getZjt_element_id() {
		return zjt_element_id;
	}

	public ElementList getElementlist() {
		return elementlist;
	}

	public void setElementlist(ElementList elementlist) {
		this.elementlist = elementlist;
	}

	public void setZjt_element_id(int zjt_element_id) {
		this.zjt_element_id = zjt_element_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ZJTPricingType getPricingType() {
		return pricingType;
	}

	public void setPricingType(ZJTPricingType pricingType) {
		this.pricingType = pricingType;
	}

	public Uom getUom() {
		return uom;
	}

	public void setUom(Uom uom) {
		this.uom = uom;
	}

	
}
