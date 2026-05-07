<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="/header" />
<link rel="stylesheet" href="/css/posts.css"> <div class="modify-container">
    <h1 class="modify-title">글 수정</h1>

    <form action="boardModifyProc" method="post">
        <input type="hidden" name="no" value="${board.no}" />
        
        <table class="modify-table">
            <tr>
                <th>작성자</th>
                <td><strong>${board.id}</strong></td>
                <th>조회수</th>
                <td>${board.hits}</td>
            </tr>
            <tr>
                <th>작성일</th>
                <td>${board.writeDate}</td>
                <th>첨부파일</th>
                <td style="color: #57606a;">
                    <c:choose>
                        <c:when test="${not empty board.fileName}">${board.fileName}</c:when>
                        <c:otherwise>없음</c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th>제목</th>
                <td colspan="3">
                    <input type="text" name="title" class="modify-input" value="${board.title}" required />
                </td>
            </tr>
            <tr>
                <th>내용</th>
                <td colspan="3">
                    <textarea rows="15" name="content" class="modify-textarea" required>${board.content}</textarea>
                </td>
            </tr>
        </table>

        <div class="modify-footer">
            <button type="button" class="btn-gh btn-default" onclick="location.href='boardForm'">목록</button>
            <button type="button" class="btn-gh btn-default" onclick="history.back()">취소</button>
            <input type="submit" class="btn-gh btn-primary" value="수정 완료">
        </div>
    </form>
</div>

<c:import url="/footer" />