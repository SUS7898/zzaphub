package com.care.boot.comments;

public class CommentsDTO {
    private Long id;             // PK
    private Long postId;         // FK (post_id)
    private Long userId;         // FK (user_id)
    private Integer parentId;   // self-ref (parent_id) - 대댓글용
    private String content;     // 내용
    private String status;      // ENUM ('NORMAL', 'BLIND', 'DELETED')
    private String createdAt;   // 작성일
    
    // 조인을 위해 추가한 필드
    private String loginId;     // 작성자 아이디 (users 테이블)
    private int likeCount;

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
    
    
    
    
}
