package com.cooldrive.db;

/**
 * HudOrderDeliverCompany entity. @author MyEclipse Persistence Tools
 */
public class HudOrderDeliverCompany extends AbstractHudOrderDeliverCompany
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public HudOrderDeliverCompany() {
	}

	/** minimal constructor */
	public HudOrderDeliverCompany(String companyName, Double deliverFee,
			Short status) {
		super(companyName, deliverFee, status);
	}

	/** full constructor */
	public HudOrderDeliverCompany(String companyName, Double deliverFee,
			Short status, String companyCode) {
		super(companyName, deliverFee, status, companyCode);
	}

}
