package com.care.boot.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired private JavaMailSender mailSender;

	@Value("${spring.mail.username:}")
	private String from;

	public void sendVerificationCode(String to, String code, String purpose) {
		if(from == null || from.trim().isEmpty()) {
			throw new IllegalStateException("spring.mail.username 설정이 필요합니다.");
		}

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject("[ZZAPHUB] 이메일 인증번호");
		message.setText("인증번호는 " + code + " 입니다.\n5분 안에 입력하세요.\n인증 목적: " + purpose);

		mailSender.send(message);
	}
}
