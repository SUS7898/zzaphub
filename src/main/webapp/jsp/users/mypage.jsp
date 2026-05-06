<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />

<link rel="stylesheet" href="/css/mypage.css">

<div class="gh-container">
    <!-- 1. 대시보드 요약 카드 -->
    <div class="gh-card stats-card">
        <div class="gh-card-header">
            <c:if test="${not empty stats.equippedTitle}">
                <span class="label-title">${stats.equippedTitle}</span>
            </c:if>
            <span class="user-name-title">${user.name}님의 대시보드</span>
        </div>
        <div class="gh-card-body stats-grid">
            <div class="stat-item">💰 <span>포인트</span> <strong>${user.point}</strong></div>
            <div class="stat-item">🎁 <span>이모티콘</span> <strong>${stats.emoticonCount}</strong></div>
            <div class="stat-item">📝 <span>작성글</span> <strong>${stats.postCount}</strong></div>
            <div class="stat-item">💬 <span>댓글</span> <strong>${stats.commentCount}</strong></div>
            <div class="stat-item">❤️ <span>좋아요</span> <strong>${stats.totalLikes}</strong></div>
            <div class="stat-item">🚨 <span>신고</span> <strong class="text-danger">${stats.totalReports}</strong></div>
        </div>
    </div>

    <!-- 2. 나의 활동 내역 -->
    <div class="gh-card activity-card">
        <div class="gh-card-header">나의 활동 내역 (최근 20개)</div>
        <div class="gh-card-body no-padding">
            <table class="gh-table">
                <thead>
                    <tr>
                        <th>분류</th>
                        <th>내용/제목</th>
                        <th>작성일</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${contents}">
                        <tr>
                            <td class="type-cell"><span class="badge-${item.type}">${item.type}</span></td>
                            <td class="content-cell"><a href="postsContent?id=${item.postId}">${item.content}</a></td>
                            <td class="date-cell">${item.createdAt}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 3. 회원 정보 수정 -->
    <div class="gh-card settings-card">
        <div class="gh-card-header">회원 정보 수정</div>
        <div class="gh-card-body">
            <form action="updateProc" method="post" class="gh-form">
                <div class="form-row">
                    <label>이름</label>
                    <input type="text" name="name" value="${user.name}">
                </div>
                <div class="form-row">
                    <label>비밀번호 변경</label>
                    <input type="password" name="pw" placeholder="새 비밀번호 (필수)">
                </div>
                <div class="form-row">
                    <label>비밀번호 확인</label>
                    <input type="password" name="confirm" placeholder="새 비밀번호 재입력">
                </div>
                <div class="form-row">
                    <label>이메일</label>
                    <input type="email" name="email" value="${user.email}">
                </div>
                <div class="form-row">
                    <label>전화번호</label>
                    <input type="text" name="phone" value="${user.phone}">
                </div>
                <div class="form-footer">
                    <button type="submit" class="btn btn-success">정보 수정 저장</button>
                </div>
            </form>
        </div>
    </div>
</div>