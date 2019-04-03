package com.cooldrive.db;

import java.sql.Timestamp;

/**
 * AbstractHudOrders entity provides the base persistence definition of the
 * HudOrders entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractHudOrders implements java.io.Serializable {

	// Fields

	private Long orderId;
	private String prepayId;
	private String transactionId;
	private Integer clientId;
	private Short orderChannel;
	private Double totalAmount;
	private Double discountAmount;
	private Double payAmount;
	private String deliverUserName;
	private String deliverAddress;
	private String deliverPhone;
	private Short deliverCompanyId;
	private String deliverCode;
	private Double deliverFee;
	private Integer invoiceId;
	private Timestamp orderTime;
	private Timestamp paymentTime;
	private Short orderStatus;

	// Constructors

	/** default constructor */
	public AbstractHudOrders() {
	}

	/** minimal constructor */
	public AbstractHudOrders(Integer clientId, Short orderChannel,
			Double totalAmount, String deliverUserName, String deliverAddress,
			String deliverPhone, Double deliverFee, Timestamp orderTime,
			Short orderStatus) {
		this.clientId = clientId;
		this.orderChannel = orderChannel;
		this.totalAmount = totalAmount;
		this.deliverUserName = deliverUserName;
		this.deliverAddress = deliverAddress;
		this.deliverPhone = deliverPhone;
		this.deliverFee = deliverFee;
		this.orderTime = orderTime;
		this.orderStatus = orderStatus;
	}

	/** full constructor */
	public AbstractHudOrders(String prepayId, String transactionId,
			Integer clientId, Short orderChannel, Double totalAmount,
			Double discountAmount, Double payAmount, String deliverUserName,
			String deliverAddress, String deliverPhone, Short deliverCompanyId,
			String deliverCode, Double deliverFee, Integer invoiceId,
			Timestamp orderTime, Timestamp paymentTime, Short orderStatus) {
		this.prepayId = prepayId;
		this.transactionId = transactionId;
		this.clientId = clientId;
		this.orderChannel = orderChannel;
		this.totalAmount = totalAmount;
		this.discountAmount = discountAmount;
		this.payAmount = payAmount;
		this.deliverUserName = deliverUserName;
		this.deliverAddress = deliverAddress;
		this.deliverPhone = deliverPhone;
		this.deliverCompanyId = deliverCompanyId;
		this.deliverCode = deliverCode;
		this.deliverFee = deliverFee;
		this.invoiceId = invoiceId;
		this.orderTime = orderTime;
		this.paymentTime = paymentTime;
		this.orderStatus = orderStatus;
	}

	// Property accessors

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getPrepayId() {
		return this.prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getClientId() {
		return this.clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Short getOrderChannel() {
		return this.orderChannel;
	}

	public void setOrderChannel(Short orderChannel) {
		this.orderChannel = orderChannel;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getDiscountAmount() {
		return this.discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getPayAmount() {
		return this.payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getDeliverUserName() {
		return this.deliverUserName;
	}

	public void setDeliverUserName(String deliverUserName) {
		this.deliverUserName = deliverUserName;
	}

	public String getDeliverAddress() {
		return this.deliverAddress;
	}

	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}

	public String getDeliverPhone() {
		return this.deliverPhone;
	}

	public void setDeliverPhone(String deliverPhone) {
		this.deliverPhone = deliverPhone;
	}

	public Short getDeliverCompanyId() {
		return this.deliverCompanyId;
	}

	public void setDeliverCompanyId(Short deliverCompanyId) {
		this.deliverCompanyId = deliverCompanyId;
	}

	public String getDeliverCode() {
		return this.deliverCode;
	}

	public void setDeliverCode(String deliverCode) {
		this.deliverCode = deliverCode;
	}

	public Double getDeliverFee() {
		return this.deliverFee;
	}

	public void setDeliverFee(Double deliverFee) {
		this.deliverFee = deliverFee;
	}

	public Integer getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Timestamp getOrderTime() {
		return this.orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public Timestamp getPaymentTime() {
		return this.paymentTime;
	}

	public void setPaymentTime(Timestamp paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Short getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

}