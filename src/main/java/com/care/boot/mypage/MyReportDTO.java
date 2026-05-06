package com.care.boot.mypage;

public class MyReportDTO {
    private String targetType;     // 게시글에서 당했는지, 댓글에서 당했는지
    private String reason;         // 신고 사유
    private Long postId;        // ★어느 글(혹은 어느 글의 댓글)인지 확인하고 이동하기 위한 글 번호
    private String status;         // 처리 상태 (대기중 등)
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


    
}