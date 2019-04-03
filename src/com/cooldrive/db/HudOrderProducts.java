package com.cooldrive.db;

/**
 * HudOrderProducts entity. @author MyEclipse Persistence Tools
 */
public class HudOrderProducts extends AbstractHudOrderProducts implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public HudOrderProducts() {
	}

	/** minimal constructor */
	public HudOrderProducts(String productCode, String productName,
			Double unitPrice, Short status) {
		super(productCode, productName, unitPrice, status);
	}

	/** full constructor */
	public HudOrderProducts(String productCode, String productName,
			String discription, Double unitPrice, Short status) {
		super(productCode, productName, discription, unitPrice, status);
	}

}
