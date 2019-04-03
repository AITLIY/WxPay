package com.cooldrive.db;

import java.sql.Timestamp;

/**
 * HudOrderClients entity. @author MyEclipse Persistence Tools
 */
public class HudOrderClients extends AbstractHudOrderClients implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public HudOrderClients() {
	}

	/** full constructor */
	public HudOrderClients(String clientCode, String clientName, Short status,
			Timestamp createTime) {
		super(clientCode, clientName, status, createTime);
	}

}
