package com.care.boot.comments;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ICommentsMapper {
    // ✅ 정의되지 않았던 메서드 추가 (파라미터 타입 Long 확인)
    List<CommentsDTO> getCommentsList(Long postId);
    
    // ✅ 댓글 등록 메서드도 함께 확인
    void insertComment(CommentsDTO dto);
}