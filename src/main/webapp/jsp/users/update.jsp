<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/user.css">

<div class="user-wrapper">
    <div class="user-box">
        <h2 class="user-heading">Update Profile</h2>
        
        <div class="user-card user-card-white">
            <form action="updateProc" method="post">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" value="${sessionScope.id}" class="user-input" readonly style="background:#f6f8fa;">
                </div>
                <div class="form-group">
                    <label>New Password</label>
                    <input type="password" name="pw" class="user-input" placeholder="변경할 비밀번호">
                </div>
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="name" value="${sessionScope.userName}" class="user-input">
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value="${sessionScope.email}" class="user-input">
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="text" name="phone" value="${sessionScope.phone}" class="user-input">
                </div>

                <button type="submit" class="btn-user">Save Changes</button>
                <button type="button" class="btn-user btn-cancel" onclick="location.href='index'">Back to Home</button>
            </form>
        </div>
    </div>
</div>
<c:import url="/footer" />