package com.app.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class UserSubscription implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
