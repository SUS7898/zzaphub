<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/user.css">

<div class="user-wrapper">
    <div class="user-box">
        <h2 class="user-heading">Join zzapHub</h2>
        
        <c:if test="${not empty msg}">
            <div class="error-msg">${msg}</div>
        </c:if>

        <div class="user-card user-card-white">
            <form action="registProc" method="post">
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
                    <input type="password" name="confirm" class="user-input" required>
                </div>
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="name" class="user-input">
                </div>
                <div class="form-group">
                    <label>Email address</label>
                    <input type="email" name="email" class="user-input">
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
<c:import url="/footer" />