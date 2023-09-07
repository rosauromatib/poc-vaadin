package com.aat.application.data.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="zjt_product")
public class ZJTProduct implements ZJTPo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int zjt_product_id;
	
	@Column
	@NotEmpty
	private String name;

	@Column
	private String description;
	
	@ManyToOne
	@JoinColumn(name="zjt_resourcetype_id", referencedColumnName = "zjt_resourcetype_id")
	private ZJTResourceType resourceType;

	@ManyToOne
	@JoinColumn(name="zjt_tripelement_id", referencedColumnName = "zjt_element_id")
	private ZJTElement tripElement;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ZJTPriceListItem> children1;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ZJTComponentLine> children;

	@Enumerated(EnumType.STRING)
	@NotNull
	private TripType tripType;

	@Column(columnDefinition = "integer default 0")
	private Integer tripdayoffset;
	
	@Column
	private Timestamp tripdate;
	
	@Column
	private Timestamp triptime;
	
	@Column
	private TripLegType triplegtype;
	
	@Column
	private BigDecimal tripleghour;
	
	@Column
	private BigDecimal triplegkm;
	
	@Column BigDecimal triplegeach;
	
	
	public int getZjt_product_id() {
		return zjt_product_id;
	}

	public void setZjt_product_id(int zjt_product_id) {
		this.zjt_product_id = zjt_product_id;
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

	public ZJTResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ZJTResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public ZJTElement getTripElement() {
		return tripElement;
	}

	public void setTripElement(ZJTElement tripElement) {
		this.tripElement = tripElement;
	}

	public TripType getTripType() {
		return tripType;
	}

	public void setTripType(TripType tripType) {
		this.tripType = tripType;
	}

	public Integer getTripdayoffset() {
		return tripdayoffset;
	}

	public void setTripdayoffset(Integer tripdayoffset) {
		this.tripdayoffset = tripdayoffset;
	}

	public Timestamp getTripdate() {
		return tripdate;
	}

	public void setTripdate(Timestamp tripdate) {
		this.tripdate = tripdate;
	}

	public Timestamp getTriptime() {
		return triptime;
	}

	public void setTriptime(Timestamp triptime) {
		this.triptime = triptime;
	}

	public TripLegType getTriplegtype() {
		return triplegtype;
	}

	public void setTriplegtype(TripLegType triplegtype) {
		this.triplegtype = triplegtype;
	}

	public BigDecimal getTripleghour() {
		return tripleghour;
	}

	public void setTripleghour(BigDecimal tripleghour) {
		this.tripleghour = tripleghour;
	}

	public BigDecimal getTriplegkm() {
		return triplegkm;
	}

	public void setTriplegkm(BigDecimal triplegkm) {
		this.triplegkm = triplegkm;
	}

	public BigDecimal getTriplegeach() {
		return triplegeach;
	}

	public void setTriplegeach(BigDecimal triplegeach) {
		this.triplegeach = triplegeach;
	}
}
