package com.care.boot.comments;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import com.care.boot.users.IUserMapper;
import com.care.boot.users.UsersDTO;

@Controller
public class CommentsController {
    @Autowired private CommentsService commentsService;
    @Autowired private IUserMapper userMapper;

    @PostMapping("commentProc")
    public String commentProc(CommentsDTO dto, HttpSession session) {
        String loginId = (String) session.getAttribute("id");
        
        // 로그인 상태가 아니면 거부
        if (loginId == null) {
            return "redirect:login";
        }

        // DB 고유 번호(id)를 가져오기 위해 유저 정보 조회
        UsersDTO user = userMapper.login(loginId);
        if (user != null) {
            dto.setUserId(user.getId()); // UsersDTO의 id(Integer)를 CommentsDTO의 userId에 세팅
            commentsService.addComment(dto);
        }

        // 다시 해당 게시글 상세 페이지로 리다이렉트
        return "redirect:postsContent?id=" + dto.getPostId();
    }
}