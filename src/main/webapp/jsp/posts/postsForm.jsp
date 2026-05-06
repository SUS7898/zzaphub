<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/posts.css">

<div class="posts-container">
    <div class="list-header">
        <h1>게시글 목록</h1>
        <button type="button" class="btn-new-post" onclick="location.href='postsWrite'">New Post</button>
    </div>

    <div class="posts-list-box">
        <div class="list-summary">
            ${totalPosts}개의 게시글이 있습니다.
        </div>
        
        <ul style="padding: 0; margin: 0;">
            <!-- 컨트롤러에서 넘겨준 게시글 리스트 반복 -->
            <c:forEach var="post" items="${postsList}">
                <li class="posts-list-item">
                    <div class="item-title">
                        <a href="postsContent?id=${post.id}">${post.title}</a>
                    </div>
                    <div class="item-meta">
                        #${post.id} opened on ${post.createdAt} by ${post.loginId} · 조회수 ${post.viewCount}
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>

    <!-- 페이지네이션 -->
    <div class="pagination-area">
        ${result}
    </div>
</div>
<c:import url="/footer" />








