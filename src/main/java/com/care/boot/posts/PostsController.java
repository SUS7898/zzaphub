package com.care.boot.posts;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.care.boot.comments.CommentsService;
import com.care.boot.interactions.IInteractionsMapper;
import com.care.boot.point.IPointMapper; // 추가
import com.care.boot.point.UserTitleDTO; // 추가
import com.care.boot.users.IUserMapper;   // 추가
import com.care.boot.users.UsersDTO;     // 추가

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PostsController {
    @Autowired private PostsService service;
    @Autowired private CommentsService commentsService;
    @Autowired private IInteractionsMapper interactionsMapper;
    @Autowired private IPointMapper pointMapper; // 포인트 매퍼 주입
    @Autowired private IUserMapper userMapper;   // 유저 매퍼 주입
    @Autowired private HttpSession session;

    // ✅ 유저 PK(Long)를 세션 아이디로 가져오는 공통 메서드
    private Long getUserId(HttpSession session) {
        String loginId = (String) session.getAttribute("id");
        if (loginId == null) return null;
        UsersDTO user = userMapper.login(loginId);
        return user != null ? user.getId() : null;
    }

    // 🚀 [마이페이지 전용] 현재 메인 티어(승급형)를 찾아 즉시 강화소로 리다이렉트
    @GetMapping("/promoteMain")
    public String promoteMain(HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        // 유저가 가진 칭호 중 가장 높은 단계의 'PROGRESSION' 타입을 찾음
        UserTitleDTO mainTier = pointMapper.getMainProgressionTitle(userId);
        
        if (mainTier == null) {
            // 비기너도 없다면 1000P 달성 유도를 위해 인벤토리로 보냄
            return "redirect:/point/inventory";
        }

        // 해당 메인 티어 ID를 들고 승급 강화소로 이동
        return "redirect:/point/enhanceView?titleId=" + mainTier.getTitleId();
    }

    // --- 게시글 관련 기존 메서드 유지 ---
    @RequestMapping("postsForm")
    public String postsForm(Model model,
            @RequestParam(value="currentPage", required = false, defaultValue = "1") String cp,
            @RequestParam(value="category", required = false, defaultValue = "ALL") String category) {
        service.postsForm(cp, category, model);
        model.addAttribute("currentCategory", category); 
        return "posts/postsForm";
    }

    @RequestMapping("postsWrite")
    public String postsWrite() {
        if(session.getAttribute("id") == null) return "redirect:login";
        return "posts/postsWrite";
    }

    @PostMapping("postsWriteProc")
    public String postsWriteProc(MultipartHttpServletRequest multi) {
        if(session.getAttribute("id") == null) return "redirect:login";
        String category = multi.getParameter("category");
        String role = (String) session.getAttribute("role");
        if ("NOTICE".equals(category) && !"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return "redirect:postsForm"; 
        }
        return service.postsWriteProc(multi);
    }

    @RequestMapping("postsContent")
    public String postsContent(@RequestParam("id") Long id, Model model) {
        PostsDTO post = service.postsContent(String.valueOf(id));
        if(post == null) return "redirect:postsForm";
        model.addAttribute("posts", post);
        model.addAttribute("comments", commentsService.getCommentsList(id));
        model.addAttribute("postLikes", interactionsMapper.getLikeCount("POST", id.intValue()));
        
        if (post.getFileName() != null && !post.getFileName().isEmpty()) {
            String fileName = post.getFileName();
            String originalName = fileName.substring(fileName.lastIndexOf("-") + 1);
            model.addAttribute("originalFileName", originalName);
            model.addAttribute("filePreview", service.getFilePreviewContent(fileName));
        }
        return "posts/postsContent";
    }

    @RequestMapping("postsDownload")
    public void postsDownload(@RequestParam("id") String id, HttpServletResponse response) {
        service.postsDownload(id, response);
    }

    @RequestMapping("postsModify")
    public String postsModify(@RequestParam("id") String id, Model model) {
        if(session.getAttribute("id") == null) return "redirect:login";
        return service.postsModify(id, model);
    }

    @PostMapping("postsModifyProc")
    public String postsModifyProc(PostsDTO posts, RedirectAttributes ra) {
        if(session.getAttribute("id") == null) return "redirect:login";
        String msg = service.postsModifyProc(posts);
        ra.addFlashAttribute("msg", msg);
        return msg.equals("게시글 수정 성공") ? "redirect:postsContent?id=" + posts.getId() : "redirect:postsModify?id=" + posts.getId();
    }

    @RequestMapping("postsDeleteProc")
    public String postsDeleteProc(@RequestParam("id") String id) {
        if(session.getAttribute("id") == null) return "redirect:login";
        String role = (String) session.getAttribute("role");
        String msg = service.postsDeleteProc(id, role);
        return msg.equals("작성자만 삭제 할 수 있습니다.") ? "redirect:postsContent?id=" + id : "redirect:postsForm";
    }
}