package com.cooldrive.db;

/**
 * AbstractHudOrderDeliverCompany entity provides the base persistence
 * definition of the HudOrderDeliverCompany entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractHudOrderDeliverCompany implements
		java.io.Serializable {

	// Fields

	private Integer companyId;
	private String companyName;
	private Double deliverFee;
	private Short status;
	private String companyCode;

	// Constructors

	/** default constructor */
	public AbstractHudOrderDeliverCompany() {
	}

	/** minimal constructor */
	public AbstractHudOrderDeliverCompany(String companyName,
			Double deliverFee, Short status) {
		this.companyName = companyName;
		this.deliverFee = deliverFee;
		this.status = status;
	}

	/** full constructor */
	public AbstractHudOrderDeliverCompany(String companyName,
			Double deliverFee, Short status, String companyCode) {
		this.companyName = companyName;
		this.deliverFee = deliverFee;
		this.status = status;
		this.companyCode = companyCode;
	}

	// Property accessors

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Double getDeliverFee() {
		return this.deliverFee;
	}

	public void setDeliverFee(Double deliverFee) {
		this.deliverFee = deliverFee;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

}