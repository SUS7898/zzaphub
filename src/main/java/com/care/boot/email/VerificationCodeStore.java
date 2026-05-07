package com.care.boot.email;

import java.time.Duration;

public interface VerificationCodeStore {
	boolean isCooldown(String purpose, String email, Duration cooldown);
	void save(String purpose, String email, String code, Duration ttl);
	VerificationStatus verify(String purpose, String email, String code, int maxFailCount);
}
