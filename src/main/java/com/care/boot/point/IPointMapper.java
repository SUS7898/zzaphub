package com.care.boot.point;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;



@Mapper
public interface IPointMapper {
    // ==========================================
    // 💰 1. 재화(포인트) 관리
    // ==========================================
    int getUserPoint(Long userId);
    void deductPoint(@Param("userId") Long userId, @Param("amount") int amount);

    // ==========================================
    // 🎁 2. 이모티콘 상점
    // ==========================================
    Integer getEmoticonPrice(Integer emoticonId);
    int checkUserEmoticon(@Param("userId") Long userId, @Param("emoticonId") Integer emoticonId);
    void insertUserEmoticon(@Param("userId") Long userId, @Param("emoticonId") Integer emoticonId);

    // ==========================================
    // 🏆 3. 칭호 획득 및 조회
    // ==========================================
    UserTitleDTO getTitleInfo(Integer titleId); // 칭호 원본 정보 조회
    UserTitleDTO getUserTitle(@Param("userId") Long userId, @Param("titleId") Integer titleId); // 내 칭호 1개 상태 조회
    java.util.List<UserTitleDTO> getMyTitles(Long userId); // 내 인벤토리(전체 칭호) 조회
    void insertUserTitle(@Param("userId") Long userId, @Param("titleId") Integer titleId); // 칭호 인벤토리에 추가
    
    // 이름으로 칭호 ID 찾기 (비기너 해금 시 사용)
    Integer getTitleIdByName(String name);
    // 가챠 뽑기 전용: 내가 없는 'GACHA' 타입 칭호 1개 랜덤 뽑기
    UserTitleDTO getRandomGachaTitle(Long userId);

    // ==========================================
    // 🔨 4. 칭호 강화 및 승급
    // ==========================================
    void updateEnhanceSuccess(@Param("userId") Long userId, @Param("titleId") Integer titleId); // 성공 (레벨+1)
    void updateEnhanceFail(@Param("userId") Long userId, @Param("titleId") Integer titleId, @Param("dropLevel") boolean dropLevel); // 실패 (카운트+1, 확률적 레벨-1)
    void deleteUserTitle(@Param("userId") Long userId, @Param("titleId") Integer titleId); // 승급 시 기존 칭호 파기

    // ==========================================
    // 🛡️ 5. 칭호 장착 시스템
    // ==========================================
    void unequipAll(Long userId); // 장착 초기화
    void equipTitle(@Param("userId") Long userId, @Param("titleId") Integer titleId); // 특정 칭호 장착
    
    // 유저의 현재 가장 높은 메인 등급(승급형 칭호) 정보를 가져옴
    UserTitleDTO getMainProgressionTitle(Long userId);
}