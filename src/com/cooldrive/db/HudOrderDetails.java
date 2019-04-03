package com.cooldrive.db;

/**
 * HudOrderDetails entity. @author MyEclipse Persistence Tools
 */
public class HudOrderDetails extends AbstractHudOrderDetails implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public HudOrderDetails() {
	}

	/** full constructor */
	public HudOrderDetails(Long orderId, Integer productId, Double unitPrice,
			Integer quantity, Short status) {
		super(orderId, productId, unitPrice, quantity, status);
	}

}
