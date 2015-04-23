package com.app.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayloadAssignment implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement
	private Account account;
	@XmlElement
	private Company company;
	@XmlElement
	private SubscriptionOrder order;
	@XmlElement
	private String configuration;
	@XmlElement
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public SubscriptionOrder getOrder() {
		return order;
	}

	public void setOrder(SubscriptionOrder order) {
		this.order = order;
	}
}
