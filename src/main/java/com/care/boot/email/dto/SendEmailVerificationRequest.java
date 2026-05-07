package com.care.boot.email.dto;

public class SendEmailVerificationRequest {
	private String email;
	private String purpose;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
}
