package com.care.boot.email.dto;

public class EmailVerificationResponse {
	private boolean success;
	private String status;
	private String message;

	public static EmailVerificationResponse ok(String status, String message) {
		EmailVerificationResponse response = new EmailVerificationResponse();
		response.setSuccess(true);
		response.setStatus(status);
		response.setMessage(message);
		return response;
	}

	public static EmailVerificationResponse fail(String status, String message) {
		EmailVerificationResponse response = new EmailVerificationResponse();
		response.setSuccess(false);
		response.setStatus(status);
		response.setMessage(message);
		return response;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
