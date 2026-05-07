<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/user.css">

<div class="user-wrapper">
    <div class="user-box">
        <h2 class="user-heading">Change password for ${targetId}</h2>
        <div class="user-card">
            <form action="resetPwProc" method="post">
                <input type="hidden" name="loginId" value="${targetId}">
                <div class="form-group">
                    <label>New password</label>
                    <input type="password" name="pw" class="user-input" required>
                </div>
                <div class="form-group">
                    <label>Confirm new password</label>
                    <input type="password" name="pwConfirm" class="user-input" required>
                </div>
                <button type="submit" class="btn-user">Update password</button>
            </form>
        </div>
    </div>
</div>
<c:import url="/footer" />