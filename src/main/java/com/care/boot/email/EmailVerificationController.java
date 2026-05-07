package com.care.boot.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.care.boot.email.dto.EmailVerificationResponse;
import com.care.boot.email.dto.SendEmailVerificationRequest;
import com.care.boot.email.dto.VerifyEmailCodeRequest;

@RestController
@RequestMapping("/api/email-verification")
public class EmailVerificationController {
	@Autowired private EmailVerificationService service;

	@PostMapping("/send")
	public EmailVerificationResponse send(@RequestBody SendEmailVerificationRequest request) {
		return service.send(request);
	}

	@PostMapping("/verify")
	public EmailVerificationResponse verify(@RequestBody VerifyEmailCodeRequest request) {
		return service.verify(request);
	}
}
