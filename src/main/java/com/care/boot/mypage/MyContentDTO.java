package com.care.boot.mypage;

public class MyContentDTO {
    private String type;           // POST(글), COMMENT(댓글), REPLY(답글)
    private Long postId;        // ★클릭 시 이동할 게시글 고유 번호 (posts의 id)
    private String content;        // 글 제목 또는 댓글 내용
    private String createdAt;      // 작성일
    // Getter, Setter 추가
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;	
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
    
}