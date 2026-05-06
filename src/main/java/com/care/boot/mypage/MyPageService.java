package com.care.boot.mypage;

import com.care.boot.users.UsersDTO;
import com.care.boot.users.IUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.util.List;

@Service
public class MyPageService {
    @Autowired private IMyPageMapper myPageMapper;
    @Autowired private IUserMapper userMapper;

    public void loadDashboard(String loginId, Model model) {
        // 1. 세션의 loginId로 전체 유저 정보 조회
        UsersDTO user = userMapper.login(loginId);
        if (user == null) return;
        
        // 2. PK(id)를 Long 타입으로 가져와서 매퍼에 전달
        Long userId = user.getId(); 

        // 3. 통계, 활동 내역, 신고 내역 조회
        MyStatsDTO stats = myPageMapper.getMyStats(userId);
        List<MyContentDTO> contents = myPageMapper.getMyContents(userId);
        List<MyReportDTO> reports = myPageMapper.getMyReports(userId);

        // 4. JSP에서 사용할 수 있도록 Model에 담기
        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        model.addAttribute("contents", contents);
        model.addAttribute("reports", reports);
        
        System.out.println("조회된 통계: " + stats);
        System.out.println("조회된 활동내역 수: " + (contents != null ? contents.size() : "null"));
    }
}