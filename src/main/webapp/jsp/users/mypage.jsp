<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="/header" />

<link rel="stylesheet" href="/css/mypage.css">

<div class="gh-container">

	<c:if
		test="${sessionScope.role eq 'ADMIN' or sessionScope.role eq 'MANAGER'}">
		<div class="gh-card admin-card">
			<div class="gh-card-header admin-card-header">👑 관리자 전용 제어판</div>
			<div class="gh-card-body admin-actions">
				<a href="/admin/reports" class="btn btn-danger">🚨 신고 관리</a> <a
					href="/admin/users" class="btn btn-primary">👥 유저 관리</a> <span
					class="admin-role-text"> 현재 권한: <strong>${sessionScope.role}</strong>
				</span>
			</div>
			<div class="gh-card-body admin-info-text">
				* <strong>신고 관리:</strong> 신고 누적 글 필터링, 부적합 게시글 삭제 및 블라인드 처리<br>
				* <strong>유저 관리:</strong> 유저 정보 열람(비밀번호 제외), 매니저 승격/강등, 악성 유저 강제 탈퇴
				처리
			</div>
		</div>
	</c:if>

	<div class="gh-card stats-card">
		<div class="gh-card-header">
			<c:if test="${not empty stats.equippedTitle}">
				<span class="label-title">${stats.equippedTitle}</span>
			</c:if>
			<span class="user-name-title">${user.name}님의 대시보드</span>
		</div>
		<div class="gh-card-body stats-grid">
			<div class="stat-item">
				💰 <span>포인트</span> <strong>${user.point}</strong>
			</div>
			<div class="stat-item">
				🎁 <span>이모티콘</span> <strong>${stats.emoticonCount}</strong>
			</div>
			<div class="stat-item">
				📝 <span>작성글</span> <strong>${stats.postCount}</strong>
			</div>
			<div class="stat-item">
				💬 <span>댓글</span> <strong>${stats.commentCount}</strong>
			</div>
			<div class="stat-item">
				❤️ <span>좋아요</span> <strong>${stats.totalLikes}</strong>
			</div>
			<div class="stat-item">
				🚨 <span>신고</span> <strong class="text-danger">${stats.totalReports}</strong>
			</div>
		</div>
	</div>

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
							<td class="content-cell"><a
								href="postsContent?id=${item.postId}">${item.content}</a></td>
							<td class="date-cell">${item.createdAt}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<div class="gh-card settings-card">
		<div class="gh-card-header">회원 정보 확인</div>
		<div class="gh-card-body">
			<form action="updateProc" method="post" class="gh-form">
				<div class="form-row">
					<label>이름</label>
					<%-- 🔒 readonly 추가 및 배경색 조절을 위해 readonly-input 클래스 추가 --%>
					<input type="text" name="name" value="${user.name}"
						class="user-input readonly-input" readonly tabindex="-1">
				</div>

				<%-- 비밀번호 변경은 확인 모드에서는 숨기거나 제거하는 것이 깔끔합니다. --%>

				<div class="form-row">
					<label>이메일</label> <input type="email" name="email"
						value="${user.email}" class="user-input readonly-input" readonly
						tabindex="-1">
				</div>
				<div class="form-row">
					<label>전화번호</label> <input type="text" name="phone"
						value="${user.phone}" class="user-input readonly-input" readonly
						tabindex="-1">
				</div>

				<div class="form-footer">
					<%-- 💡 버튼을 누르면 실제 수정이 가능한 'update' 페이지로 이동하게끔 변경 --%>
					<button type="button" class="btn btn-primary"
						onclick="location.href='userUpdate'">정보 수정</button>
				</div>
			</form>
		</div>
	</div>
	
	<%-- ⚠️ Danger Zone: 회원 탈퇴 구역 --%>
    <div class="gh-card danger-card" style="margin-top: 30px; border-color: #cf222e;">
        <div class="gh-card-header" style="background-color: #ffebe9; color: #cf222e; border-bottom-color: rgba(207,34,46,0.1);">
            Danger Zone
        </div>
        <div class="gh-card-body" style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <strong style="display: block;">Delete Account</strong>
                <span style="font-size: 13px; color: #57606a;">계정을 삭제하면 모든 활동 내역과 포인트가 영구적으로 삭제됩니다.</span>
            </div>
            <%-- 컨트롤러의 @RequestMapping("delete")로 연결 --%>
            <button type="button" class="btn btn-danger" 
                    onclick="location.href='/delete'" 
                    style="background-color: #cf222e; color: white; border: 1px solid rgba(31,35,40,0.15); padding: 5px 12px; border-radius: 6px; font-weight: 600; cursor: pointer;">
                Delete Account
            </button>
        </div>
    </div>
</div> ```
	
	
</div>

<c:import url="/footer" />