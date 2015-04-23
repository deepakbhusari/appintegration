package com.app.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonManagedReference;
import com.google.inject.internal.Sets;

@Entity
@Table(name = "APP_USER")
@XmlRootElement(name = "creator")
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement
	@Id
	@Column(unique = true)
	private String uuid;
	@XmlElement
	private String firstName;
	@XmlElement
	private String lastName;
	@XmlElement
	private String email;
	@XmlElement
	private String openId;
	@XmlElement
	private String language;

	@XmlElement
	@JsonManagedReference
	private Set<Entry> attributes = Sets.newHashSet();

	public Set<Entry> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<Entry> attributes) {
		this.attributes = attributes;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
