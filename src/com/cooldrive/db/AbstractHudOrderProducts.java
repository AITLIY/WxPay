package com.cooldrive.db;

/**
 * AbstractHudOrderProducts entity provides the base persistence definition of
 * the HudOrderProducts entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractHudOrderProducts implements java.io.Serializable {

	// Fields

	private Integer productId;
	private String productCode;
	private String productName;
	private String discription;
	private Double unitPrice;
	private Short status;

	// Constructors

	/** default constructor */
	public AbstractHudOrderProducts() {
	}

	/** minimal constructor */
	public AbstractHudOrderProducts(String productCode, String productName,
			Double unitPrice, Short status) {
		this.productCode = productCode;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.status = status;
	}

	/** full constructor */
	public AbstractHudOrderProducts(String productCode, String productName,
			String discription, Double unitPrice, Short status) {
		this.productCode = productCode;
		this.productName = productName;
		this.discription = discription;
		this.unitPrice = unitPrice;
		this.status = status;
	}

	// Property accessors

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDiscription() {
		return this.discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

}