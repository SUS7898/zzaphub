package com.care.boot.mypage;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyPageController {
    @Autowired private MyPageService myPageService;
    @Autowired private HttpSession session;

   //mypage
    @RequestMapping("mypage") 
    public String myPage(Model model) {
        // 세션에서 로그인한 아이디를 가져옴
        String loginId = (String) session.getAttribute("id"); 
        
        if(loginId == null) {
            return "redirect:login"; 
        }

        // ✅ 여기서 데이터를 로드함 (이제 로그가 찍힐 겁니다!)
        myPageService.loadDashboard(loginId, model);
        
        // ✅ 실제 사용 중인 JSP 경로로 수정 (users/userInfo.jsp 인지 확인 필요)
        return "users/mypage";  
    }
}