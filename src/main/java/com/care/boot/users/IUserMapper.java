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


}












