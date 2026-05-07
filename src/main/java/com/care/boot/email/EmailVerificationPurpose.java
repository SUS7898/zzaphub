package com.care.boot.email;

public enum EmailVerificationPurpose {
	SIGNUP,
	PASSWORD_RESET,
	EMAIL_CHANGE;

	public static EmailVerificationPurpose from(String value) {
		if(value == null || value.trim().isEmpty()) {
			return SIGNUP;
		}
		return EmailVerificationPurpose.valueOf(value.trim().toUpperCase());
	}
}
