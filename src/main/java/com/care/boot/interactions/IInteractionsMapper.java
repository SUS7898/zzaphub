package com.care.boot.interactions;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IInteractionsMapper {
    // 좋아요 관련
    int checkLikeExists(ReactionsDTO dto);
    void insertLike(ReactionsDTO dto);
    void deleteLike(ReactionsDTO dto);
    int getLikeCount(@Param("targetType") String targetType, @Param("targetId") int targetId);

    // 신고 관련
    void insertReport(ReportsDTO dto);
}
