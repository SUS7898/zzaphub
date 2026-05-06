package com.care.boot.mypage;

public class MyStatsDTO {
    private String equippedTitle;  // 장착 중인 칭호 이름
    private int emoticonCount;     // 보유 이모티콘 수
    private int postCount;         // 작성글 수
    private int commentCount;      // 작성 댓글 수 (parent_id 가 null)
    private int replyCount;        // 작성 답글 수 (parent_id 가 있는 경우)
    private int totalLikes;        // 받은 총 좋아요 수
    private int totalReports;      // 받은 총 신고 수
    // Getter, Setter 추가
	public String getEquippedTitle() {
		return equippedTitle;
	}
	public void setEquippedTitle(String equippedTitle) {
		this.equippedTitle = equippedTitle;
	}
	public int getEmoticonCount() {
		return emoticonCount;
	}
	public void setEmoticonCount(int emoticonCount) {
		this.emoticonCount = emoticonCount;
	}
	public int getPostCount() {
		return postCount;
	}
	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public int getTotalLikes() {
		return totalLikes;
	}
	public void setTotalLikes(int totalLikes) {
		this.totalLikes = totalLikes;
	}
	public int getTotalReports() {
		return totalReports;
	}
	public void setTotalReports(int totalReports) {
		this.totalReports = totalReports;
	}
    
}