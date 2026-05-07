package com.care.boot.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.care.boot.users.IUserMapper;

import java.util.List;




@Service
public class CommentsService {
    @Autowired private ICommentsMapper mapper;
    @Autowired private IUserMapper userMapper;

    public List<CommentsDTO> getCommentsList(Long postId) {
        return mapper.getCommentsList(postId);
    }

    public void addComment(CommentsDTO dto) {
        if(dto.getParentId() != null && dto.getParentId() == 0) {
            dto.setParentId(null);
        }
        mapper.insertComment(dto);
     // 2. 💰 포인트 지급 (예: 댓글 작성 시 10포인트)
        // 주의: userMapper가 주입되어 있어야 합니다 (@Autowired)
        userMapper.addPoint(dto.getUserId(), 10);
    }

    // ✅ 댓글 삭제 로직 (작성자 혹은 관리자/매니저만 가능)
    public String deleteComment(Long id, String sessionId, String role) {
        CommentsDTO comment = mapper.getCommentById(id);
        if(comment == null) return "이미 삭제된 댓글입니다.";

        boolean isAdmin = "ADMIN".equals(role) || "MANAGER".equals(role);
        boolean isOwner = sessionId != null && sessionId.equals(comment.getLoginId());

        if(isAdmin || isOwner) {
            mapper.deleteComment(id);
            return "삭제 성공";
        }
        return "권한이 없습니다.";
    }

    // ✅ 댓글 수정 로직 (작성자만 가능)
    public void modifyComment(CommentsDTO dto, String sessionId) {
        CommentsDTO check = mapper.getCommentById(dto.getId());
        if(check != null && sessionId != null && sessionId.equals(check.getLoginId())) {
            mapper.modifyComment(dto);
        }
    }
}