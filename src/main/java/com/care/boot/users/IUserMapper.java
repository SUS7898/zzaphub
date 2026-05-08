package com.care.boot.users;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IUserMapper {

	int registProc(UsersDTO user);

	UsersDTO login(String id);

	ArrayList<UsersDTO> userInfo(@Param("begin")int begin, @Param("end")int end,
			@Param("select")String select, @Param("search")String search);

	int totalCount(@Param("select")String select, @Param("search")String search);

	int updateProc(UsersDTO user);

	int deleteProc(String id);
	
	// 특정 유저의 포인트를 추가하는 메서드
	void addPoint(@Param("id") Long id, @Param("point") int point);


	int countByEmail(@Param("email") String email);

	int updateVerifiedByEmail(@Param("email") String email);

	int countBlacklistByEmail(@Param("email") String email);

	int insertBlacklist(@Param("email") String email, @Param("reason") String reason);

	String findIdByEmail(String email); // 이메일로 아이디 검색
	int updatePw(@Param("loginId") String loginId, @Param("pw") String pw); // 비밀번호 변경
	
	// 1. 유저 권한(Role) 업데이트
    void updateUserRole(@Param("id") Long id, @Param("role") String role);

    // 2. 관리자/매니저 지정 시 기존 칭호 모두 해제
    void unequipAllTitles(Long userId);

    // 3. 전용 칭호 지급 (이미 있으면 무시)
    void insertSpecialTitle(@Param("userId") Long userId, @Param("titleId") int titleId);

    // 4. 전용 칭호 즉시 장착
    void equipSpecialTitle(@Param("userId") Long userId, @Param("titleId") int titleId);
    
    // 특정 유저의 관리자/매니저(특수) 칭호를 인벤토리에서 삭제
    void deleteSpecialTitles(Long userId);
}











