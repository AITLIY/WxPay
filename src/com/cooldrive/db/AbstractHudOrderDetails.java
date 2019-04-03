package com.cooldrive.db;

/**
 * AbstractHudOrderDetails entity provides the base persistence definition of
 * the HudOrderDetails entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractHudOrderDetails implements java.io.Serializable {

	// Fields

	private Long orderDetailId;
	private Long orderId;
	private Integer productId;
	private Double unitPrice;
	private Integer quantity;
	private Short status;

	// Constructors

	/** default constructor */
	public AbstractHudOrderDetails() {
	}

	/** full constructor */
	public AbstractHudOrderDetails(Long orderId, Integer productId,
			Double unitPrice, Integer quantity, Short status) {
		this.orderId = orderId;
		this.productId = productId;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.status = status;
	}

	// Property accessors

	public Long getOrderDetailId() {
		return this.orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

}