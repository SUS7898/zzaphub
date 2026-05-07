package com.care.boot.email;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.care.boot.email.dto.EmailVerificationResponse;
import com.care.boot.email.dto.SendEmailVerificationRequest;
import com.care.boot.email.dto.VerifyEmailCodeRequest;
import com.care.boot.users.IUserMapper;

@Service
public class EmailVerificationService {
	private static final Duration CODE_TTL = Duration.ofMinutes(5);
	private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(30);
	private static final int MAX_FAIL_COUNT = 5;

	@Autowired private VerificationCodeStore codeStore;
	@Autowired private MailService mailService;
	@Autowired private IUserMapper userMapper;

	private final SecureRandom random = new SecureRandom();

	// 💡 이메일 발송 핵심 로직
	public EmailVerificationResponse send(SendEmailVerificationRequest request) {
		String email = normalizeEmail(request.getEmail());
		EmailVerificationPurpose purpose;

		try {
			purpose = EmailVerificationPurpose.from(request.getPurpose());
		} catch (IllegalArgumentException e) {
			return EmailVerificationResponse.fail("invalid_purpose", "지원하지 않는 인증 목적입니다.");
		}

		// 🛡️ 1. 중복 체크: 회원가입 시 이미 가입된 이메일인지 확인
		if (purpose == EmailVerificationPurpose.SIGNUP && userMapper.countByEmail(email) > 0) {
			return EmailVerificationResponse.fail("already_exists", "이미 사용 중인 이메일입니다.");
		}

		// 🛡️ 2. 존재 체크: 비밀번호 찾기 등(SIGNUP 제외)은 가입된 유저여야 함
		if (requiresExistingUser(purpose) && userMapper.countByEmail(email) == 0) {
			return EmailVerificationResponse.fail("not_found", "가입된 이메일 정보가 없습니다.");
		}

		if (codeStore.isCooldown(purpose.name(), email, RESEND_COOLDOWN)) {
			return EmailVerificationResponse.fail("cooldown", "인증번호는 30초 후 다시 요청할 수 있습니다.");
		}

		String code = createCode();
		try {
			// 🚀 자동으로 이메일 전송 실행
			mailService.sendVerificationCode(email, code, purpose.name());
			codeStore.save(purpose.name(), email, code, CODE_TTL);
			return EmailVerificationResponse.ok("sent", "인증번호가 이메일로 발송되었습니다.");
		} catch (Exception e) {
			return EmailVerificationResponse.fail("mail_error", "이메일 발송에 실패했습니다. SMTP 설정을 확인하세요.");
		}
	}

	// 💡 인증번호 검증 로직
	@Transactional
	public EmailVerificationResponse verify(VerifyEmailCodeRequest request) {
		String email = normalizeEmail(request.getEmail());
		String code = request.getCode() == null ? "" : request.getCode().trim();
		EmailVerificationPurpose purpose = EmailVerificationPurpose.from(request.getPurpose());

		VerificationStatus status = codeStore.verify(purpose.name(), email, code, MAX_FAIL_COUNT);

		if (status == VerificationStatus.SUCCESS) {
			return EmailVerificationResponse.ok("verified", "이메일 인증이 완료되었습니다.");
		}

		if (status == VerificationStatus.LOCKED) {
			userMapper.insertBlacklist(email, "인증번호 5회 실패로 차단");
			return EmailVerificationResponse.fail("blacklisted", "인증 실패 횟수 초과로 해당 이메일이 차단되었습니다.");
		}

		return EmailVerificationResponse.fail("mismatch", "인증번호가 일치하지 않거나 만료되었습니다.");
	}

	// 🛡️ 목적별 유저 존재 여부 체크 기준 (중복 제거됨)
	private boolean requiresExistingUser(EmailVerificationPurpose purpose) {
		// 회원가입(SIGNUP)은 유저가 없는 상태여야 하므로 false를 리턴
		return purpose != EmailVerificationPurpose.SIGNUP;
	}

	private boolean isImplementedPurpose(EmailVerificationPurpose purpose) {
		return purpose == EmailVerificationPurpose.SIGNUP;
	}

	private String createCode() {
		return String.format("%04d", random.nextInt(10000));
	}

	private String normalizeEmail(String email) {
		if (email == null) return "";
		return email.trim().toLowerCase();
	}
}