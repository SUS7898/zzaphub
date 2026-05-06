<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<link rel="stylesheet" href="${context}css/main.css">

<div class="gh-container">
    <div class="gh-main-content">
        <!-- 왼쪽: 공지사항 섹션 -->
        <aside class="gh-sidebar">
            <div class="gh-card">
                <div class="gh-card-header">📢 Announcements</div>
                <ul class="gh-list">
                    <c:choose>
                        <c:when test="${empty notices}">
                            <li class="gh-list-item empty">등록된 공지사항이 없습니다.</li>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="notice" items="${notices}">
                                <li class="gh-list-item">
                                    <a href="postsContent?id=${notice.id}">${notice.title}</a>
                                    <span class="gh-date">${notice.createdAt}</span>
                                </li>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </aside>

        <!-- 오른쪽: 인기 게시글 섹션 -->
        <section class="gh-feed">
            <div class="gh-card">
                <div class="gh-card-header">🔥 Popular Posts</div>
                <div class="gh-card-body">
                    <c:choose>
                        <c:when test="${empty popularPosts}">
                            <p class="empty">인기 게시글이 아직 없습니다.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="post" items="${popularPosts}">
                                <div class="gh-post-item">
                                    <span class="gh-category-label">${post.category}</span>
                                    <a href="postsContent?id=${post.id}" class="gh-post-title">${post.title}</a>
                                    <div class="gh-post-meta">
                                        by ${post.loginId} · 조회수 ${post.viewCount} · ${post.createdAt}
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>
    </div>
</div>