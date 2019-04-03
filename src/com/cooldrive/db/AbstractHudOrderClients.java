package com.cooldrive.db;

import java.sql.Timestamp;

/**
 * AbstractHudOrderClients entity provides the base persistence definition of
 * the HudOrderClients entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractHudOrderClients implements java.io.Serializable {

	// Fields

	private Integer clientId;
	private String clientCode;
	private String clientName;
	private Short status;
	private Timestamp createTime;

	// Constructors

	/** default constructor */
	public AbstractHudOrderClients() {
	}

	/** full constructor */
	public AbstractHudOrderClients(String clientCode, String clientName,
			Short status, Timestamp createTime) {
		this.clientCode = clientCode;
		this.clientName = clientName;
		this.status = status;
		this.createTime = createTime;
	}

	// Property accessors

	public Integer getClientId() {
		return this.clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}