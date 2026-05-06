<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="${context}css/login.css">

<div class="login-wrapper">
    <div class="login-box">
        <h2 class="login-heading">Sign in to zzapHub</h2>
        
        <c:if test="${not empty msg}">
            <div class="login-error-msg">${msg}</div>
        </c:if>

        <div class="login-card">
            <form action="loginProc" method="post">
                <div class="form-group">
                    <label for="loginId">Username or email address</label>
                    <input type="text" name="loginId" id="loginId" class="login-input" required>
                </div>

                <div class="form-group">
                    <div class="label-wrapper">
                        <label for="pw">Password</label>
                        <a href="${context}findPw" class="find-link">Forgot password?</a>
                    </div>
                    <input type="password" name="pw" id="pw" class="login-input" required>
                </div>

                <button type="submit" class="btn-login">Sign in</button>
            </form>
            
            <div class="social-login">
                <p>Or sign in with</p>
                <a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=YOUR_ID&redirect_uri=YOUR_URL">
                    <img src="https://k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg" alt="카카오 로그인">
                </a>
            </div>
        </div>

        <div class="signup-prompt">
            New to zzapHub? <a href="${context}regist">Create an account</a>.
        </div>
        
        <div class="find-id-prompt">
             <a href="${context}findId">Find my Username</a>
        </div>
    </div>
</div>

<c:import url="/footer" />