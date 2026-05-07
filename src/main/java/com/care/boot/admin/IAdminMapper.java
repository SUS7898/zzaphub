package com.care.boot.admin;

import com.care.boot.users.UsersDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface IAdminMapper {
    // 1. 전체 유저 목록 조회
    List<UsersDTO> getUserList();

    // 2. 특정 유저 권한 변경 (USER, MANAGER, ADMIN)
    void updateUserRole(@Param("userId") Integer userId, @Param("newRole") String newRole);

    // 3. 신고 누적 통계 조회 (minCount 이상인 것만)
    List<Map<String, Object>> getReportSummary(@Param("minCount") int minCount);

    // 4. 신고 처리 상태 변경
    void updateReportStatus(@Param("targetId") Integer targetId, @Param("status") String status);
    
    // 5. 유저 강제 탈퇴 (삭제 대신 상태 변경 처리 권장)
    void userBan(@Param("id") Integer id);
    
 // IAdminMapper.java 인터페이스에 추가
    UsersDTO getUserById(@Param("id") Integer id);
    
    void updateUserPoint(@Param("userId") Integer userId, @Param("amount") Integer amount);
}