package com.app.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventAssignment implements Serializable{

	private static final long serialVersionUID = 1L;
	@XmlElement
	private String type;
	@XmlElement
	private MarketPlace marketplace;
	@XmlElement
	private String flag;
	@XmlElement(name = "creator")
	private User creator;
	@XmlElement
	private PayloadAssignment payload;

	public PayloadAssignment getPayload() {
		return payload;
	}

	public void setPayload(PayloadAssignment payload) {
		this.payload = payload;
	}

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
}
