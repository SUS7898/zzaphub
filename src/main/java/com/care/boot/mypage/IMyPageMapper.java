package com.care.boot.mypage;

import org.apache.ibatis.annotations.Mapper;

import com.care.boot.posts.PostsDTO;

import java.util.List;

@Mapper
public interface IMyPageMapper {
    
    // 1. 내 통계 요약 가져오기 (xml의 id="getMyStats" 와 매칭)
    MyStatsDTO getMyStats(Long userId);
    
    // 2. 내가 쓴 글/댓글/답글 목록 가져오기 (xml의 id="getMyContents" 와 매칭)
    List<MyContentDTO> getMyContents(Long userId);
    
    // 3. 신고 당한 사유 목록 가져오기 (xml의 id="getMyReports" 와 매칭)
    List<MyReportDTO> getMyReports(Long userId);
    
    // XML에만 있고 인터페이스에 없던 메서드 추가
    List<PostsDTO> getMyPosts(Long userId);
    int getMyPostCount(Long userId);
    
}