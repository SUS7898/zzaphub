<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/user.css">
<%-- 공통 이메일 JS 포함 --%>
<script src="/js/emailVerification.js"></script>

<div class="user-wrapper">
    <div class="user-box">
        <h2 class="user-heading">Join zzapHub</h2>
        
        <c:if test="${not empty msg}">
            <div class="error-msg">${msg}</div>
        </c:if>

        <div class="user-card user-card-white">
            <form action="registProc" method="post" onsubmit="return validateForm()">
                <div class="form-group">
                    <label>Username (ID) *</label>
                    <input type="text" name="loginId" class="user-input" required>
                </div>
                <div class="form-group">
                    <label>Password *</label>
                    <input type="password" name="pw" id="pw" class="user-input" required>
                </div>
                <div class="form-group">
                    <label>Confirm Password *</label>
                    <input type="password" name="confirm" id="confirm" class="user-input" required>
                </div>
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="name" class="user-input">
                </div>

                <%-- 📧 이메일 인증 구역 --%>
                <div class="form-group">
                    <label>Email address *</label>
                    <div style="display: flex; gap: 8px;">
                        <input type="email" name="email" id="email" class="user-input" required placeholder="example@gmail.com">
                        <button type="button" class="btn-user" style="width: 100px; margin-top: 0;" onclick="handleSendEmail()">인증 요청</button>
                    </div>
                </div>

                <%-- 🔐 인증번호 입력 구역 (성공 시 노출) --%>
                <div class="form-group" id="verify-area" style="display: none;">
                    <label>Verification Code *</label>
                    <div style="display: flex; gap: 8px;">
                        <input type="text" id="authCode" class="user-input" placeholder="숫자 4자리">
                        <button type="button" class="btn-user" style="width: 100px; margin-top: 0; background-color: #2da44e;" onclick="handleVerifyCode()">코드 확인</button>
                    </div>
                    <small id="verify-msg" style="display: block; margin-top: 5px;"></small>
                </div>

                <div class="form-group">
                    <label>Phone Number</label>
                    <input type="text" name="phone" class="user-input" placeholder="010-0000-0000">
                </div>

                <button type="submit" class="btn-user">Create account</button>
                <button type="button" class="btn-user btn-cancel" onclick="location.href='index'">Cancel</button>
            </form>
        </div>
    </div>
</div>

<script>
let isEmailVerified = false;

// 1. [인증 요청] 버튼 클릭 시 실행: 자동으로 메일 발송
async function handleSendEmail() {
    const email = document.getElementById('email').value;
    if(!email || !email.includes('@')) return alert("올바른 이메일을 입력하세요.");

    try {
        const result = await sendEmailVerification(email, 'SIGNUP');
        alert(result.message);
        if(result.success) {
            document.getElementById('verify-area').style.display = 'block';
            document.getElementById('email').readOnly = true; // 인증 중 이메일 고정
        }
    } catch (e) {
        alert("서버 통신 중 오류가 발생했습니다.");
    }
}

// 2. [코드 확인] 버튼 클릭 시 실행
async function handleVerifyCode() {
    const email = document.getElementById('email').value;
    const code = document.getElementById('authCode').value;
    const msgArea = document.getElementById('verify-msg');

    try {
        const result = await verifyEmailCode(email, code, 'SIGNUP');
        if(result.success) {
            alert("이메일 인증 성공!");
            msgArea.innerText = "✅ 인증되었습니다.";
            msgArea.style.color = "#2da44e";
            document.getElementById('authCode').readOnly = true;
            isEmailVerified = true;
        } else {
            msgArea.innerText = "❌ " + result.message;
            msgArea.style.color = "#cf222e";
        }
    } catch (e) {
        alert("서버 통신 중 오류가 발생했습니다.");
    }
}

// 3. 최종 가입 전 체크
function validateForm() {
    if(!isEmailVerified) {
        alert("이메일 인증을 먼저 완료해주세요.");
        return false;
    }
    if(document.getElementById('pw').value !== document.getElementById('confirm').value) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }
    return true;
}
</script>

<c:import url="/footer" />