<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/user.css">

<div class="user-wrapper">
    <div class="user-box">
        <h2 class="user-heading">Find your ID</h2>
        <div class="user-card">
            <div class="form-group">
                <label for="email">Enter your recovery email</label>
                <input type="email" id="email" class="user-input" placeholder="example@gmail.com">
            </div>
            <button type="button" class="btn-user" onclick="handleFindId()">Find Username</button>
            <div id="idResult" class="result-msg" style="display:none;"></div>
        </div>
        <div class="helper-links">
            <a href="login">Back to Sign in</a>
        </div>
    </div>
</div>

<script>
function handleFindId() {
    const email = document.getElementById('email').value;
    const resultDiv = document.getElementById('idResult');
    
    if(!email) { alert("이메일을 입력해주세요."); return; }

    fetch('findIdProc?email=' + email, { method: 'POST' })
    .then(res => res.text())
    .then(data => {
        resultDiv.style.display = 'block';
        resultDiv.className = data.includes('[') ? 'result-msg msg-success' : 'result-msg msg-error';
        resultDiv.innerText = data;
    });
}
</script>
<c:import url="/footer" />