<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/posts.css">

<div class="posts-view-container">
    <div class="posts-header-area">
        <h1 class="posts-title">${posts.title} <span style="color:#57606a; font-weight:300;">#${posts.id}</span></h1>
        <div class="posts-meta">
            <span class="btn-gh" style="background:#2da44e; color:#fff; border:none; border-radius:2em; cursor:default;">Open</span>
            <strong>${posts.loginId}</strong>님이 작성 · 조회수 ${posts.viewCount}
            <button type="button" class="btn-gh" onclick="handleLike('POST', ${posts.id})">
                ❤️ <span id="post-like-count">${postLikes}</span>
            </button>
            <button type="button" class="btn-gh" onclick="handleReport('POST', ${posts.id})">🚨 신고</button>
        </div>
    </div>

    <!-- 메인 게시글 -->
    <div class="posts-bubble">
        <div class="posts-bubble-header"><strong>${posts.loginId}</strong> commented on ${posts.createdAt}</div>
        <div class="posts-bubble-body">${posts.content}</div>
    </div>

    <!-- 댓글 목록 -->
    <div class="comments-section">
        <c:forEach var="comment" items="${comments}">
            <div class="comment-wrapper ${comment.parentId != null ? 'reply-item' : ''}">
                <div class="posts-bubble">
                    <div class="posts-bubble-header">
                        <strong>${comment.loginId}</strong>
                        <div style="display:flex; gap:8px;">
                            <button class="btn-gh" onclick="handleLike('COMMENT', ${comment.id})">
                                ❤️ <span id="like-COMMENT-${comment.id}">${comment.likeCount}</span>
                            </button>
                            <button class="btn-gh" onclick="toggleReply(${comment.id})">Reply</button>
                        </div>
                    </div>
                    <div class="posts-bubble-body">${comment.content}</div>
                </div>
                <!-- 답글 입력창 -->
                <div id="reply-form-${comment.id}" style="display:none; margin: 0 0 20px 45px;">
                    <form action="commentProc" method="post">
                        <input type="hidden" name="postId" value="${posts.id}">
                        <input type="hidden" name="parentId" value="${comment.id}">
                        <textarea name="content" class="gh-input" placeholder="답글을 남겨보세요..." required></textarea>
                        <div style="text-align:right;"><button type="submit" class="btn-gh btn-comment">Reply</button></div>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- 새 댓글 작성 -->
    <c:if test="${not empty sessionScope.id}">
        <div class="posts-bubble" style="margin-top:40px;">
            <div class="posts-bubble-header"><strong>Write a comment</strong></div>
            <form action="commentProc" method="post" style="padding:16px;">
                <input type="hidden" name="postId" value="${posts.id}">
                <textarea name="content" class="gh-input" placeholder="Leave a comment" required></textarea>
                <div style="text-align:right;"><button type="submit" class="btn-gh btn-comment">Comment</button></div>
            </form>
        </div>
    </c:if>
</div>

<script>
function handleLike(type, id) {
    fetch('/interactions/like', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({targetType: type, targetId: id})
    }).then(res => res.json()).then(data => {
        if(data.res === "login_required") return alert("로그인이 필요합니다.");
        const target = type === 'POST' ? 'post-like-count' : 'like-COMMENT-' + id;
        document.getElementById(target).innerText = data.count;
    });
}
function toggleReply(id) {
    const f = document.getElementById('reply-form-' + id);
    f.style.display = f.style.display === 'none' ? 'block' : 'none';
}
</script>
<c:import url="/footer" />