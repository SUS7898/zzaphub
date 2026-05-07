async function sendEmailVerification(email, purpose = 'SIGNUP') {
	const response = await fetch('/api/email-verification/send', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			email: email,
			purpose: purpose
		})
	});

	return await response.json();
}

async function verifyEmailCode(email, code, purpose = 'SIGNUP') {
	const response = await fetch('/api/email-verification/verify', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			email: email,
			purpose: purpose,
			code: code
		})
	});

	return await response.json();
}
