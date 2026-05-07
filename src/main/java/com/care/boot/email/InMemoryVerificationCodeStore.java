package com.care.boot.email;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class InMemoryVerificationCodeStore implements VerificationCodeStore {
	private final ConcurrentHashMap<String, VerificationCodeEntry> store = new ConcurrentHashMap<>();

	@Override
	public boolean isCooldown(String purpose, String email, Duration cooldown) {
		VerificationCodeEntry entry = store.get(key(purpose, email));
		if(entry == null) {
			return false;
		}
		Instant now = Instant.now();
		if(entry.expiresAt.isBefore(now)) {
			store.remove(key(purpose, email));
			return false;
		}
		return entry.sentAt.plus(cooldown).isAfter(now);
	}

	@Override
	public void save(String purpose, String email, String code, Duration ttl) {
		Instant now = Instant.now();
		store.put(key(purpose, email), new VerificationCodeEntry(code, now, now.plus(ttl)));
	}

	@Override
	public VerificationStatus verify(String purpose, String email, String code, int maxFailCount) {
		String key = key(purpose, email);
		VerificationCodeEntry entry = store.get(key);
		if(entry == null) {
			return VerificationStatus.NOT_FOUND;
		}

		if(entry.expiresAt.isBefore(Instant.now())) {
			store.remove(key);
			return VerificationStatus.EXPIRED;
		}

		if(entry.code.equals(code)) {
			store.remove(key);
			return VerificationStatus.SUCCESS;
		}

		entry.failCount++;
		if(entry.failCount >= maxFailCount) {
			store.remove(key);
			return VerificationStatus.LOCKED;
		}

		return VerificationStatus.MISMATCH;
	}

	private String key(String purpose, String email) {
		return purpose + ":" + email;
	}

	private static class VerificationCodeEntry {
		private final String code;
		private final Instant sentAt;
		private final Instant expiresAt;
		private int failCount;

		private VerificationCodeEntry(String code, Instant sentAt, Instant expiresAt) {
			this.code = code;
			this.sentAt = sentAt;
			this.expiresAt = expiresAt;
			this.failCount = 0;
		}
	}
}
