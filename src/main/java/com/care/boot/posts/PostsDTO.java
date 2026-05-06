package com.care.boot.posts;
/*
CREATE TABLE `posts` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer NOT NULL,
  `category` varchar(255),
  `title` varchar(255) NOT NULL,
  `content` text,
  `view_count` integer DEFAULT 0,
  `status` ENUM ('NORMAL', 'BLIND', 'DELETED') DEFAULT 'NORMAL',
  `created_at` timestamp DEFAULT (now())
);
 */
import java.sql.Timestamp;

public class PostsDTO {
    private Long id;            // DB의 id (게시글 번호)
    private Long userId;        // DB의 user_id (작성자 고유번호)
    private String loginId;     // 작성자 아이디 (조인용)
    private String category;
    private String title;
    private String content;
    private int viewCount;      // DB: view_count
    private String status;      // DB: status (NORMAL, BLIND, DELETED)
    private String createdAt;   // DB: created_at
    private String fileName;
    
    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
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
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... 나머지 필드들도 동일하게 생성
}

/*
INSERT INTO board_quiz 
VALUES(1, 'admin 게시글 제목', 'admin 게시글 내용', 'admin', '2023-09-22', 0, '');
INSERT INTO board_quiz 
VALUES(2, 'user1 게시글 제목', 'user1 게시글 내용', 'user1', '2023-09-22', 0, '');
INSERT INTO board_quiz 
VALUES(3, 'user2 게시글 제목', 'user2 게시글 내용', 'user2', '2023-09-23', 0, '');
INSERT INTO board_quiz 
VALUES(4, 'test1 게시글 제목', 'test1 게시글 내용', 'test1', '2023-09-23', 0, '');
INSERT INTO board_quiz 
VALUES(5, 'test2 게시글 제목', 'test2 게시글 내용', 'test2', '2023-09-24', 0, '');
COMMIT;
*/
