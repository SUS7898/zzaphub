<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="/header" />
<link rel="stylesheet" href="/css/admin.css">



<div class="admin-container">
    <h2 style="margin-bottom: 20px;">👥 유저 관리 시스템</h2>

    <table class="admin-table">
        <thead>
            <tr>
                <th>고유번호</th>
                <th>로그인 ID</th>
                <th>이름</th>
                <th>이메일</th>
                <th>현재 권한</th>
                <th>계정 상태</th>
                <th>권한 변경/제재</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="u" items="${userList}">
                <tr>
                    <td>#${u.id}</td>
                    <td>
                        <a href="/admin/userDetail?id=${u.id}" style="color: #0969da; font-weight: 600; text-decoration: none;">
                            ${u.loginId}
                        </a>
                    </td>
                    <td>${u.name}</td>
                    <td>${u.email}</td>
                    <td>
                        <c:choose>
                            <%-- 1. 대상이 실제 관리자(ADMIN)인 경우: 변경 불가 --%>
                            <c:when test="${u.role eq 'ADMIN'}">
                                <span class="badge-admin-fixed">ADMIN</span>
                            </c:when>

                            <%-- 2. 본인이 'ADMIN'인 경우에만 수정 가능 --%>
                            <c:when test="${sessionScope.role eq 'ADMIN'}">
                                <form action="/updateRoleProc" method="post">
                                    <input type="hidden" name="userId" value="${u.id}">
                                    <select name="newRole" class="role-select" onchange="confirmRoleChange(this)">
                                        <option value="USER" ${u.role == 'USER' ? 'selected' : ''}>USER</option>
                                        <option value="MANAGER" ${u.role == 'MANAGER' ? 'selected' : ''}>MANAGER</option>
                                    </select>
                                </form>
                            </c:when>

                            <%-- 3. 본인이 'MANAGER'인 경우: 텍스트로만 표시 --%>
                            <c:otherwise>
                                <span class="badge-role-read">${u.role}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <span class="status-badge ${u.role != 'BANNED' ? 'status-active' : 'status-banned'}">
                            ${u.role != 'BANNED' ? '정상' : '정지됨'}
                        </span>
                    </td>
                    <td>
                        <%-- 🛡️ 강제 탈퇴 버튼: 대상이 ADMIN이 아니기만 하면 ADMIN/MANAGER 모두에게 노출 --%>
                        <c:if test="${u.role ne 'ADMIN'}">
                            <button class="btn-admin btn-ban" onclick="banUser('${u.id}')">강제탈퇴</button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<c:if test="${not empty msg}">
    <script>alert("${msg}");</script>
</c:if>

<script>
    function confirmRoleChange(selectElement) {
        const userName = selectElement.closest('tr').cells[2].innerText; 
        const newRole = selectElement.value;
        
        if (confirm(userName + "님의 권한을 " + newRole + "(으)로 변경하시겠습니까?")) {
            selectElement.form.submit();
        } else {
            location.reload(); 
        }
    }

    function banUser(id) {
        if (confirm("정말로 이 회원을 강제 탈퇴 처리하시겠습니까?")) {
            location.href = "/admin/userBan?id=" + id;
        }
    }
</script>
<c:import url="/footer" />