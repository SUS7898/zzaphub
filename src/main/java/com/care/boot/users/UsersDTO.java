package com.care.boot.users;

import java.time.LocalDateTime;

/*
CREATE TABLE `users` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `login_id` varchar(255) UNIQUE NOT NULL,
  `pw` varchar(255) NOT NULL,
  `name` varchar(255),
  `email` varchar(255) UNIQUE,
  `phone` varchar(255),
  `point` integer DEFAULT 0,
  `role` ENUM ('USER', 'ADMIN') DEFAULT 'USER',
  `is_verified` boolean DEFAULT false,
  `created_at` timestamp DEFAULT (now())
);
*/
public class UsersDTO {
	private Long id;
	private String loginId;
	private String pw;           // 또는 password로 길게 쓰는 것도 좋습니다.
	private String name;
	private String email;
	private String phone;
	private Integer point;
	private String role;         // ENUM을 따로 만들었다면 Role role; 로 사용
	private Boolean isVerified;
	private LocalDateTime createdAt; // import java.time.LocalDateTime; 필요
	private String confirm;      // [추가됨] 비밀번호 확인용 변수
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Boolean getIsVerified() {
		return isVerified;
	}
	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	public String getConfirm() { //확인용 변수
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	
	
	
}

/*

INSERT INTO db_quiz VALUES('user1', '1111', '원유저', '서울시 유일구', '010-1111-1111');
INSERT INTO db_quiz VALUES('user2', '2222', '이유저', '서울시 유이구', '010-2222-1111');
INSERT INTO db_quiz VALUES('user3', '3333', '삼유저', '서울시 유삼구', '010-3333-1111');
INSERT INTO db_quiz VALUES('user4', '4444', '사유저', '서울시 유사구', '010-4444-1111');
INSERT INTO db_quiz VALUES('user5', '5555', '오유저', '서울시 유오구', '010-5555-1111');

INSERT INTO db_quiz VALUES('test1', '1111', '테스트1', '서울시 테일구', '010-1111-1111');
INSERT INTO db_quiz VALUES('test2', '2222', '테스트2', '서울시 테이구', '010-1111-2222');
INSERT INTO db_quiz VALUES('test3', '3333', '테스트3', '서울시 테삼구', '010-1111-3333');
INSERT INTO db_quiz VALUES('test4', '4444', '테스트4', '서울시 테사구', '010-1111-4444');
COMMIT;
*/





