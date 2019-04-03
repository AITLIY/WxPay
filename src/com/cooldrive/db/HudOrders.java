package com.cooldrive.db;

import java.sql.Timestamp;

/**
 * HudOrders entity. @author MyEclipse Persistence Tools
 */
public class HudOrders extends AbstractHudOrders implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public HudOrders() {
	}

	/** minimal constructor */
	public HudOrders(Integer clientId, Short orderChannel, Double totalAmount,
			String deliverUserName, String deliverAddress, String deliverPhone,
			Double deliverFee, Timestamp orderTime, Short orderStatus) {
		super(clientId, orderChannel, totalAmount, deliverUserName,
				deliverAddress, deliverPhone, deliverFee, orderTime,
				orderStatus);
	}

	/** full constructor */
	public HudOrders(String prepayId, String transactionId, Integer clientId,
			Short orderChannel, Double totalAmount, Double discountAmount,
			Double payAmount, String deliverUserName, String deliverAddress,
			String deliverPhone, Short deliverCompanyId, String deliverCode,
			Double deliverFee, Integer invoiceId, Timestamp orderTime,
			Timestamp paymentTime, Short orderStatus) {
		super(prepayId, transactionId, clientId, orderChannel, totalAmount,
				discountAmount, payAmount, deliverUserName, deliverAddress,
				deliverPhone, deliverCompanyId, deliverCode, deliverFee,
				invoiceId, orderTime, paymentTime, orderStatus);
	}

}
