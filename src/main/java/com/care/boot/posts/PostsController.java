package com.care.boot.posts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.care.boot.comments.CommentsService; // 추가
import com.care.boot.interactions.IInteractionsMapper; // 추가

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PostsController {
    @Autowired private PostsService service;
    @Autowired private CommentsService commentsService; // 댓글 서비스 주입
    @Autowired private IInteractionsMapper interactionsMapper; // 좋아요 조회를 위한 매퍼 주입
    @Autowired private HttpSession session;
    
    @RequestMapping("postsForm")
    public String postsForm(Model model,
            @RequestParam(value="currentPage", required = false, defaultValue = "1") String cp) {
        // 서비스 내부에서 model.addAttribute("postsList", ...) 가 실행되는지 확인 필요
        service.postsForm(cp, model); 
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
        return service.postsWriteProc(multi);
    }
    
    @RequestMapping("postsContent")
    public String postsContent(@RequestParam("id") Long id, Model model) { // String -> Long 변경
        PostsDTO post = service.postsContent(String.valueOf(id));
        if(post == null) return "redirect:postsForm";
        
        // ✅ [중요] 게시글 상세 데이터 외에 필요한 데이터들 추가
        model.addAttribute("posts", post);
        model.addAttribute("comments", commentsService.getCommentsList(id)); // 댓글 리스트 추가
        model.addAttribute("postLikes", interactionsMapper.getLikeCount("POST", id.intValue())); // 좋아요 수 추가
        
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
        
        if(msg.equals("게시글 수정 성공"))
            return "redirect:postsContent?id=" + posts.getId();
        
        return "redirect:postsModify?id=" + posts.getId();
    }
    
    @RequestMapping("postsDeleteProc")
    public String postsDeleteProc(@RequestParam("id") String id) {
        if(session.getAttribute("id") == null) return "redirect:login";
        
        String msg = service.postsDeleteProc(id);
        if(msg.equals("작성자만 삭제 할 수 있습니다."))
            return "redirect:postsContent?id=" + id;
        
        return "redirect:postsForm";
    }
}