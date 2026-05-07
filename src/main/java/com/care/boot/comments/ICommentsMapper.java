package com.care.boot.comments;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ICommentsMapper {
    List<CommentsDTO> getCommentsList(Long postId);
    void insertComment(CommentsDTO dto);
    
    // ✅ 추가된 메서드들
    CommentsDTO getCommentById(Long id); // 권한 체크용 조회
    void deleteComment(Long id);         // 댓글 삭제
    void modifyComment(CommentsDTO dto); // 댓글 수정
}