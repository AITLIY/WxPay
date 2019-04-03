package com.cooldrive.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class HudOrderInfo {

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
	private String deliverCompanyName;
	private String deliverCompanyCode;
	private String deliverCode;
	private Double deliverFee;
	private Integer invoiceId;
	private Timestamp orderTime;
	private Timestamp paymentTime;
	private Short orderStatus;
	
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public Short getOrderChannel() {
		return orderChannel;
	}
	public void setOrderChannel(Short orderChannel) {
		this.orderChannel = orderChannel;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	public String getDeliverUserName() {
		return deliverUserName;
	}
	public void setDeliverUserName(String deliverUserName) {
		this.deliverUserName = deliverUserName;
	}
	public String getDeliverAddress() {
		return deliverAddress;
	}
	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}
	public String getDeliverPhone() {
		return deliverPhone;
	}
	public void setDeliverPhone(String deliverPhone) {
		this.deliverPhone = deliverPhone;
	}
	public Short getDeliverCompanyId() {
		return deliverCompanyId;
	}
	public void setDeliverCompanyId(Short deliverCompanyId) {
		this.deliverCompanyId = deliverCompanyId;
	}
	public String getDeliverCompanyName() {
		return deliverCompanyName;
	}
	public void setDeliverCompanyName(String deliverCompanyName) {
		this.deliverCompanyName = deliverCompanyName;
	}
	public String getDeliverCompanyCode() {
		return deliverCompanyCode;
	}
	public void setDeliverCompanyCode(String deliverCompanyCode) {
		this.deliverCompanyCode = deliverCompanyCode;
	}
	public String getDeliverCode() {
		return deliverCode;
	}
	public void setDeliverCode(String deliverCode) {
		this.deliverCode = deliverCode;
	}
	public Double getDeliverFee() {
		return deliverFee;
	}
	public void setDeliverFee(Double deliverFee) {
		this.deliverFee = deliverFee;
	}
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	public Timestamp getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	public Timestamp getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Timestamp paymentTime) {
		this.paymentTime = paymentTime;
	}
	public Short getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}
	

}
