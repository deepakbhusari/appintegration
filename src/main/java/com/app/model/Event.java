package com.app.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class Event implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "creator")
	private User creator;
	@XmlElement
	private MarketPlace marketplace;
	@XmlElement
	private Payload payload;
	@XmlElement
	private String returnUrl;

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	@XmlElement
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public MarketPlace getMarketplace() {
		return marketplace;
	}

	public void setMarketplace(MarketPlace marketplace) {
		this.marketplace = marketplace;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}
}
