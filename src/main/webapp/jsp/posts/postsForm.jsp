<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />

<link rel="stylesheet" href="/css/posts.css">

<div class="posts-container">
    <div class="list-header">
        <h1>게시글 목록</h1>
        <c:if test="${not empty sessionScope.id}">
            <button type="button" class="btn-new-post" onclick="location.href='postsWrite'">New Post</button>
        </c:if>
    </div>

    <nav class="category-tabs">
        <a href="postsForm?category=ALL" class="${currentCategory == 'ALL' ? 'active' : ''}">All</a>
        <a href="postsForm?category=NOTICE" class="${currentCategory == 'NOTICE' ? 'active' : ''}">Notice</a>
        <a href="postsForm?category=TECH" class="${currentCategory == 'TECH' ? 'active' : ''}">Tech</a>
        <a href="postsForm?category=LIFE" class="${currentCategory == 'LIFE' ? 'active' : ''}">Life</a>
        <a href="postsForm?category=QNA" class="${currentCategory == 'QNA' ? 'active' : ''}">Q&A</a>
    </nav>

    <div class="posts-list-box">
        <div class="list-summary">
            <span class="category-label">${currentCategory}</span> 카테고리에 
            <strong>${totalPosts}</strong>개의 게시글이 있습니다.
        </div>
        
        <ul class="posts-ul">
            <c:choose>
                <%-- 게시글이 없는 경우 --%>
                <c:when test="${empty postsList}">
                    <li class="posts-list-item empty-list">
                        <div class="empty-state">
                            <p>등록된 게시글이 없습니다.</p>
                            <small>첫 번째 게시글의 주인공이 되어보세요!</small>
                        </div>
                    </li>
                </c:when>
                
                <%-- 게시글 리스트 출력 --%>
                <c:otherwise>
                    <c:forEach var="post" items="${postsList}">
                        <li class="posts-list-item">
                            <div class="item-main">
                                <div class="item-title">
                                    <%-- 공지사항일 경우 빨간 뱃지 표시 --%>
                                    <c:if test="${post.category == 'NOTICE'}">
                                        <span class="badge-NOTICE">공지</span>
                                    </c:if>
                                    <a href="postsContent?id=${post.id}">${post.title}</a>
                                    
                                    <%-- 카테고리 표시 (전체보기 모드일 때 유용) --%>
                                    <c:if test="${currentCategory == 'ALL'}">
                                        <span class="item-category-tag">${post.category}</span>
                                    </c:if>
                                </div>
                                <div class="item-meta">
                                    #${post.id} opened on ${post.createdAt} by <strong>${post.loginId}</strong> 
                                    · 조회수 ${post.viewCount}
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>

    <div class="pagination-area">
        ${result}
    </div>
</div>

<c:import url="/footer" />