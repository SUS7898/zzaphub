<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/user.css">

<div class="user-wrapper">
    <div class="user-box">
        <h2 class="user-heading" style="color:#cf222e;">Delete Account</h2>
        
        <div class="error-msg" style="margin-bottom:20px;">
            <strong>Warning:</strong> 이 작업은 취소할 수 없습니다. 계정의 모든 데이터가 영구적으로 삭제됩니다.
        </div>

        <div class="user-card">
            <form action="deleteProc" method="post">
                <div class="form-group">
                    <label>Confirm Username</label>
                    <input type="text" name="loginId" value="${sessionScope.id}" class="user-input" readonly>
                </div>
                <div class="form-group">
                    <label>Enter Password to Confirm</label>
                    <input type="password" name="pw" class="user-input" required>
                </div>
                <div class="form-group">
                    <label>Re-enter Password</label>
                    <input type="password" name="confirm" class="user-input" required>
                </div>

                <button type="submit" class="btn-user" style="background-color:#cf222e;">I understand, delete this account</button>
                <button type="button" class="btn-user btn-cancel" onclick="history.back()">Cancel</button>
            </form>
        </div>
    </div>
</div>
<c:import url="/footer" />