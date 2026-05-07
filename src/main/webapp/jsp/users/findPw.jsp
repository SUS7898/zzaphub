<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/user.css">
<%-- 💡 이미 만들어두신 공통 JS 파일을 반드시 포함해야 합니다 --%>
<script src="/js/emailVerification.js"></script>

<div class="user-wrapper">
    <div class="user-box">
        <h2 class="user-heading">Reset your password</h2>
        <div class="user-card">
            <form action="/resetPw" method="post" id="pwForm">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="loginId" id="loginId" class="user-input" required>
                </div>
                <div class="form-group">
                    <label>Email address</label>
                    <div style="display: flex; gap: 8px;">
                        <input type="email" id="email" class="user-input" required>
                        <button type="button" class="btn-verify" onclick="handleSendCode()" style="width:120px;">인증번호 전송</button>
                    </div>
                </div>
                <div class="form-group" id="authSection" style="display:none; margin-top:15px;">
                    <label>Verification Code</label>
                    <div style="display: flex; gap: 8px;">
                        <input type="text" id="authCode" class="user-input" placeholder="숫자 4자리">
                        <button type="button" class="btn-user" onclick="handleVerifyAndSubmit()" style="width:120px; margin-top:0;">인증 확인</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
// 1. [인증번호 전송] 클릭 시: EmailVerificationController의 /api/email-verification/send 호출
async function handleSendCode() {
    const email = document.getElementById('email').value;
    if(!email) return alert("이메일을 입력하세요.");
    
    // 💡 패키지 내 emailVerification.js의 함수 사용 (목적: PASSWORD_RESET)
    const result = await sendEmailVerification(email, 'PASSWORD_RESET');
    alert(result.message);
    if(result.success) {
        document.getElementById('authSection').style.display = 'block';
    }
}

// 2. [인증 확인] 클릭 시: /api/email-verification/verify 호출 후 성공하면 폼 제출
async function handleVerifyAndSubmit() {
    const email = document.getElementById('email').value;
    const code = document.getElementById('authCode').value;
    
    const result = await verifyEmailCode(email, code, 'PASSWORD_RESET');
    if(result.success) {
        alert("인증에 성공했습니다. 비밀번호 재설정 페이지로 이동합니다.");
        document.getElementById('pwForm').submit();
    } else {
        alert(result.message);
    }
}
</script>
<c:import url="/footer" />