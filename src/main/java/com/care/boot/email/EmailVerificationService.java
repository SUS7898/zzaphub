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

	public EmailVerificationResponse send(SendEmailVerificationRequest request) {
		String email = normalizeEmail(request.getEmail());
		EmailVerificationPurpose purpose;

		try {
			purpose = EmailVerificationPurpose.from(request.getPurpose());
		} catch (IllegalArgumentException e) {
			return EmailVerificationResponse.fail("invalid_purpose", "지원하지 않는 인증 목적입니다.");
		}

		if(isImplementedPurpose(purpose) == false) {
			return EmailVerificationResponse.fail("not_implemented", "v1에서는 회원가입 이메일 인증만 지원합니다.");
		}

		String validationMessage = validateEmail(email);
		if(validationMessage != null) {
			return EmailVerificationResponse.fail("invalid_email", validationMessage);
		}

		if(userMapper.countBlacklistByEmail(email) > 0) {
			return EmailVerificationResponse.fail("blacklisted", "인증 실패 횟수 초과로 차단된 이메일입니다.");
		}

		if(requiresExistingUser(purpose) && userMapper.countByEmail(email) == 0) {
			return EmailVerificationResponse.fail("not_found", "가입된 이메일이 없습니다.");
		}

		if(codeStore.isCooldown(purpose.name(), email, RESEND_COOLDOWN)) {
			return EmailVerificationResponse.fail("cooldown", "인증번호는 30초 후 다시 보낼 수 있습니다.");
		}

		String code = createCode();
		try {
			mailService.sendVerificationCode(email, code, purpose.name());
			codeStore.save(purpose.name(), email, code, CODE_TTL);
			return EmailVerificationResponse.ok("sent", "인증번호를 이메일로 보냈습니다.");
		} catch (Exception e) {
			return EmailVerificationResponse.fail("mail_error", "이메일 발송에 실패했습니다. SMTP 설정을 확인하세요.");
		}
	}

	@Transactional
	public EmailVerificationResponse verify(VerifyEmailCodeRequest request) {
		String email = normalizeEmail(request.getEmail());
		String code = request.getCode() == null ? "" : request.getCode().trim();
		EmailVerificationPurpose purpose;

		try {
			purpose = EmailVerificationPurpose.from(request.getPurpose());
		} catch (IllegalArgumentException e) {
			return EmailVerificationResponse.fail("invalid_purpose", "지원하지 않는 인증 목적입니다.");
		}

		if(isImplementedPurpose(purpose) == false) {
			return EmailVerificationResponse.fail("not_implemented", "v1에서는 회원가입 이메일 인증만 지원합니다.");
		}

		String validationMessage = validateEmail(email);
		if(validationMessage != null) {
			return EmailVerificationResponse.fail("invalid_email", validationMessage);
		}

		if(code.matches("\\d{4}") == false) {
			return EmailVerificationResponse.fail("invalid_code", "인증번호 4자리를 입력하세요.");
		}

		if(userMapper.countBlacklistByEmail(email) > 0) {
			return EmailVerificationResponse.fail("blacklisted", "인증 실패 횟수 초과로 차단된 이메일입니다.");
		}

		VerificationStatus status = codeStore.verify(purpose.name(), email, code, MAX_FAIL_COUNT);
		if(status == VerificationStatus.SUCCESS) {
			if(purpose == EmailVerificationPurpose.SIGNUP) {
				int updated = userMapper.updateVerifiedByEmail(email);
				if(updated == 0) {
					return EmailVerificationResponse.fail("not_found", "가입된 이메일이 없습니다.");
				}
			}
			return EmailVerificationResponse.ok("verified", "이메일 인증이 완료되었습니다.");
		}

		if(status == VerificationStatus.LOCKED) {
			userMapper.insertBlacklist(email, "이메일 인증번호 5회 실패");
			return EmailVerificationResponse.fail("blacklisted", "인증 실패 5회 초과로 이메일이 차단되었습니다.");
		}

		if(status == VerificationStatus.EXPIRED) {
			return EmailVerificationResponse.fail("expired", "인증번호가 만료되었습니다. 다시 발송하세요.");
		}

		if(status == VerificationStatus.NOT_FOUND) {
			return EmailVerificationResponse.fail("not_found", "발송된 인증번호가 없습니다.");
		}

		return EmailVerificationResponse.fail("mismatch", "인증번호가 일치하지 않습니다.");
	}

	private boolean requiresExistingUser(EmailVerificationPurpose purpose) {
		return purpose == EmailVerificationPurpose.SIGNUP;
	}

	private boolean isImplementedPurpose(EmailVerificationPurpose purpose) {
		return purpose == EmailVerificationPurpose.SIGNUP;
	}

	private String createCode() {
		return String.format("%04d", random.nextInt(10000));
	}

	private String normalizeEmail(String email) {
		if(email == null) {
			return "";
		}
		return email.trim().toLowerCase();
	}

	private String validateEmail(String email) {
		if(email.isEmpty()) {
			return "이메일을 입력하세요.";
		}
		if(email.contains("@") == false || email.contains(".") == false) {
			return "이메일 형식이 올바르지 않습니다.";
		}
		return null;
	}
}
