package com.care.boot;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.care.boot.posts.PostsDTO;
import com.care.boot.posts.PostsService;

@Controller
public class HomeController {

    // 💡 게시글 정보를 가져오기 위해 PostsService를 주입합니다.
    @Autowired private PostsService postsService;
    
    /**
     * 메인 페이지 접속 (localhost/ 또는 localhost/index)
     */
    @RequestMapping({"/", "index"})
    public String index(Model model) {
        System.out.println("======= [홈 컨트롤러] 메인 페이지 진입 =======");
        
        // 1. 공지사항 최근 5개 가져오기 (카테고리 'NOTICE')
        List<PostsDTO> notices = postsService.getPostsByCategory("NOTICE", 5);
        model.addAttribute("notices", notices);
        
        // 2. 인기 게시글 최근 5개 가져오기 (조회수 기준)
        List<PostsDTO> popularPosts = postsService.getPopularPosts(5);
        model.addAttribute("popularPosts", popularPosts);
        
        // 3. index.jsp 화면 반환
        return "index"; 
    }
    
    /**
     * 공통 헤더
     */
    @RequestMapping("header")
    public String header() {
        return "default/header";
    }

    /**
     * 공통 메인 (단독 호출용)
     */
    @RequestMapping("main")
    public String main() {
        return "default/main";
    }

    /**
     * 공통 푸터
     */
    @RequestMapping("footer")
    public String footer() {
        return "default/footer";
    }
}