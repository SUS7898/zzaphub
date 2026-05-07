<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/admin.css">

<div class="admin-container">
    <h2 style="margin-bottom: 20px;">🚨 신고 통합 관리</h2>

    <div class="admin-filter-bar">
        <strong>⚡ 필터:</strong>
        <form action="/admin/reports" method="get">
            <input type="number" name="minCount" value="${minCount}" style="width:50px; padding:4px; border-radius:4px; border:1px solid #d0d7de;">
            회 이상 신고된 내역만 보기
            <button type="submit" class="btn-admin" style="background:#f6f8fa; margin-left:10px;">필터 적용</button>
        </form>
    </div>

    <table class="admin-table">
        <thead>
            <tr>
                <th>분류</th>
                <th>대상 번호</th>
                <th>신고 누적</th>
                <th>마지막 상태</th>
                <th>관리 액션</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="r" items="${reportSummary}">
                <tr>
                    <td><span class="status-badge" style="background:#fff8c5; color:#9a6700;">${r.targetType}</span></td>
                    <td><a href="/postsContent?id=${r.targetId}" target="_blank">#${r.targetId} 원본보기 🔗</a></td>
                    <td><strong style="color:#cf222e;">${r.reportCount}회</strong></td>
                    <td>${r.status}</td>
                    <td>
                        <div style="display:flex; gap:8px;">
                            <button class="btn-admin btn-resolve" onclick="resolveReport('${r.targetId}')">해결 완료</button>
                            <button class="btn-admin btn-ban" onclick="deletePostByAdmin('${r.targetId}')">즉시 삭제</button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<script>
function resolveReport(id) {
    if(confirm("이 게시글의 신고를 처리 완료하시겠습니까?")) {
        location.href = "/admin/reportResolve?id=" + id;
    }
}
function deletePostByAdmin(id) {
    if(confirm("부적합한 게시글로 판단하여 즉시 삭제(DELETED) 처리하시겠습니까?")) {
        location.href = "/postsDeleteProc?id=" + id;
    }
}
</script>
<c:import url="/footer" />