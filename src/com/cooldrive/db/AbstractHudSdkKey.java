package com.cooldrive.db;

/**
 * AbstractHudSdkKey entity provides the base persistence definition of the
 * HudSdkKey entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractHudSdkKey implements java.io.Serializable {

	// Fields

	private Integer id;
	private String username;
	private Short appType;
	private String appid;
	private String apiKey;
	private Short status;

	// Constructors

	/** default constructor */
	public AbstractHudSdkKey() {
	}

	/** full constructor */
	public AbstractHudSdkKey(String username, Short appType, String appid,
			String apiKey, Short status) {
		this.username = username;
		this.appType = appType;
		this.appid = appid;
		this.apiKey = apiKey;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Short getAppType() {
		return this.appType;
	}

	public void setAppType(Short appType) {
		this.appType = appType;
	}

	public String getAppid() {
		return this.appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

}