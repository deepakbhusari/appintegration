package com.app.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is for sending the response after transaction has been processed
 * by the application
 *
 */
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegrationAppResponse {
	@XmlElement
	private Boolean success;
	// The error codes are USER_ALREADY_EXISTS , USER_NOT_FOUND ,
	// ACCOUNT_NOT_FOUND , MAX_USERS_REACHED , UNAUTHORIZED ,
	// OPERATION_CANCELED , CONFIGURATION_ERROR , INVALID_RESPONSE ,
	// UNKNOWN_ERROR , PENDING
	@XmlElement
	private String errorCode;
	@XmlElement
	private String message;
	@XmlElement
	private String accountIdentifier;

	public IntegrationAppResponse() {
	}

	public IntegrationAppResponse(Boolean result, String errorCode,
			String message, String accountIdentifier) {
		this.success = result;
		this.errorCode = errorCode;
		this.message = message;
		this.accountIdentifier = accountIdentifier;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAccountIdentifier() {
		return accountIdentifier;
	}

	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}
}
