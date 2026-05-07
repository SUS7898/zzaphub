<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:import url="/header" />
<link rel="stylesheet" href="/css/admin.css">

<div class="admin-container">
    <div style="margin-bottom: 20px;">
        <a href="/admin/users" style="text-decoration:none; color:#57606a;">← 유저 목록으로 돌아가기</a>
    </div>

    <div class="gh-card">
        <div class="gh-card-header admin-card-header">👤 유저 상세 정보 [#${targetUser.id}]</div>
        <div class="gh-card-body">
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <div>
                    <p><strong>아이디:</strong> ${targetUser.loginId}</p>
                    <p><strong>이름:</strong> ${targetUser.name}</p>
                    <p><strong>이메일:</strong> ${targetUser.email}</p>
                </div>
                <div>
                    <p><strong>전화번호:</strong> ${targetUser.phone}</p>
                    <p><strong>현재 포인트:</strong> <strong style="color:#0969da; font-size:1.2em;">${targetUser.point} P</strong></p>
                    <p><strong>가입일:</strong> ${targetUser.createdAt}</p>
                </div>
            </div>
        </div>
    </div>

    <div class="gh-card" style="border-color: #0969da; margin-top: 20px;">
        <div class="gh-card-header" style="background: #ddf4ff; color: #0969da;">💰 포인트 관리 (지급 및 차감)</div>
        <div class="gh-card-body">
            <form action="/admin/updatePoint" method="post" style="display: flex; gap: 10px; align-items: center;">
                <input type="hidden" name="userId" value="${targetUser.id}">
                <label>변동 금액:</label>
                <input type="number" name="amount" placeholder="예: 100 또는 -50" required 
                       style="padding: 6px; border: 1px solid #d0d7de; border-radius: 6px;">
                <button type="submit" class="btn-admin btn-resolve">적용하기</button>
                <span style="font-size: 12px; color: #57606a;">* 양수를 입력하면 지급, 음수를 입력하면 차감됩니다.</span>
            </form>
        </div>
    </div>
</div>

<c:import url="/footer" />