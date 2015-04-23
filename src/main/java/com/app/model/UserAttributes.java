package com.app.model;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.google.inject.internal.Sets;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserAttributes implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement
	private Set<Entry> entry = Sets.newLinkedHashSet();

	public Set<Entry> getEntry() {
		return entry;
	}

	public void setEntry(Set<Entry> entry) {
		this.entry = entry;
	}
}
