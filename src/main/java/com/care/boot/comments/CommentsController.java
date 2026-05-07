package com.care.boot.comments;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.care.boot.users.IUserMapper;
import com.care.boot.users.UsersDTO;

@Controller
public class CommentsController {
    @Autowired private CommentsService commentsService;
    @Autowired private IUserMapper userMapper;

    @PostMapping("commentProc")
    public String commentProc(CommentsDTO dto, HttpSession session) {
        String loginId = (String) session.getAttribute("id");
        if (loginId == null) return "redirect:login";

        UsersDTO user = userMapper.login(loginId);
        if (user != null) {
            dto.setUserId(Long.valueOf(user.getId())); 
            commentsService.addComment(dto);
        }
        return "redirect:postsContent?id=" + dto.getPostId();
    }

    // ✅ 댓글 삭제 요청 처리
    @RequestMapping("commentDelete")
    public String commentDelete(@RequestParam("id") Long id, @RequestParam("postId") Long postId, HttpSession session) {
        String sessionId = (String) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        
        if(sessionId == null) return "redirect:login";

        commentsService.deleteComment(id, sessionId, role);
        return "redirect:postsContent?id=" + postId;
    }

    // ✅ 댓글 수정 요청 처리
    @PostMapping("commentModifyProc")
    public String commentModifyProc(CommentsDTO dto, HttpSession session) {
        String sessionId = (String) session.getAttribute("id");
        if(sessionId == null) return "redirect:login";
        
        commentsService.modifyComment(dto, sessionId);
        return "redirect:postsContent?id=" + dto.getPostId();
    }
}