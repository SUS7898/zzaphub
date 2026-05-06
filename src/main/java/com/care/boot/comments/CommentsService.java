package com.care.boot.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentsService {
    @Autowired private ICommentsMapper mapper;

    // 댓글 리스트 가져오기
    public List<CommentsDTO> getCommentsList(Long postId) {
        return mapper.getCommentsList(postId);
    }

    // 댓글 또는 답글 저장
    public void addComment(CommentsDTO dto) {
        // parentId가 0으로 넘어오는 경우 처리 (MyBatis에서 null로 인식하도록)
        if(dto.getParentId() != null && dto.getParentId() == 0) {
            dto.setParentId(null);
        }
        mapper.insertComment(dto);
    }
}