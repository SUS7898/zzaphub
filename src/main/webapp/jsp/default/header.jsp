<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url var="context" value="/"/>

<!-- 분리된 CSS 파일 연결 -->
<link rel="stylesheet" href="${context}css/header.css">

<header class="gh-header">
    <div class="gh-left-nav">
        <a href="${context}index" class="gh-logo">
            <svg height="32" viewBox="0 0 16 16" width="32" style="fill: white; vertical-align: middle; margin-right:8px;">
                <path d="M8 0c4.42 0 8 3.58 8 8a8.013 8.013 0 0 1-5.45 7.59c-.4.08-.55-.17-.55-.38 0-.27.01-1.13.01-2.2 0-.75-.25-1.23-.54-1.48 1.78-.2 3.65-.88 3.65-3.95 0-.88-.31-1.59-.82-2.15.08-.2.36-1.02-.08-2.12 0 0-.67-.22-2.2.82-.64-.18-1.32-.27-2-.27-.68 0-1.36.09-2 .27-1.53-1.03-2.2-.82-2.2-.82-.44 1.1-.16 1.92-.08 2.12-.51.56-.82 1.28-.82 2.15 0 3.06 1.86 3.75 3.64 3.95-.23.2-.44.55-.51 1.07-.46.21-1.61.55-2.33-.66-.15-.24-.6-.83-1.23-.82-.67.01-.27.38.01.53.34.19.73.9.82 1.13.16.45.68 1.31 2.69.94 0 .67.01 1.3.01 1.49 0 .21-.15.45-.55.38A7.995 7.995 0 0 1 0 8c0-4.42 3.58-8 8-8Z"></path>
            </svg>
            zzapHub Test
        </a>
        <a href="${context}postsForm" class="gh-nav-link">게시글</a>
    </div>

    <div class="gh-right-actions">
        <c:choose>
            <c:when test="${empty sessionScope.id}">
                <a href="${context}login" class="btn-auth">Sign in</a>
                <a href="${context}regist" class="btn-auth btn-signup">Sign up</a>
            </c:when>
            <c:otherwise>
                <div class="user-profile" onclick="location.href='${context}mypage?loginId=${sessionScope.id}'">
                    <span>${sessionScope.id}님</span>
                    <div style="width: 20px; height: 20px; background: #ccc; border-radius: 50%;"></div>
                </div>
                <a href="${context}logout" class="btn-auth">Logout</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>