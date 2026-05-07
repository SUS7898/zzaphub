package com.care.boot.interactions;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/interactions")
public class InteractionsController {
    
    @Autowired private InteractionsService service;

    @PostMapping("/like")
    public Map<String, Object> like(@RequestBody ReactionsDTO dto, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 세션 값 가져오기
        Object sessionVal = session.getAttribute("userNo"); 
        System.out.println("세션에 들어있는 userNo : " + sessionVal);
        
        // 세션이 없으면 로그인 필요
        if (sessionVal == null) { 
            result.put("res", "login_required"); 
            return result; 
        }

        // 2. 안전한 형변환 및 예외 처리
        try {
            Integer userNo = Integer.parseInt(String.valueOf(sessionVal));
            System.out.println("안전하게 변환된 userNo : " + userNo);
            
            dto.setUserId(userNo);
            result.put("res", "success");
            result.put("count", service.toggleLike(dto));
            
        } catch (NumberFormatException e) {
            // 만약 숫자로 바꿀 수 없는 이상한 값이 들어있다면 에러 대신 로그인 요구
            result.put("res", "login_required");
        }
        
        return result;
    }

    @PostMapping("/report")
    public Map<String, Object> report(@RequestBody ReportsDTO dto, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        Object sessionVal = session.getAttribute("userNo");
        
        if (sessionVal == null) { 
            result.put("res", "login_required"); 
            return result; 
        }

        try {
            Integer userNo = Integer.parseInt(String.valueOf(sessionVal));
            dto.setReporterId(userNo);
            service.submitReport(dto);
            result.put("res", "success");
            
        } catch (NumberFormatException e) {
            result.put("res", "login_required");
        }
        
        return result;
    }
}