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
        Integer userNo = (Integer) session.getAttribute("userNo"); 
        if (userNo == null) { result.put("res", "login_required"); return result; }
        dto.setUserId(userNo);
        result.put("res", "success");
        result.put("count", service.toggleLike(dto));
        return result;
    }

    @PostMapping("/report")
    public Map<String, Object> report(@RequestBody ReportsDTO dto, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Integer userNo = (Integer) session.getAttribute("userNo");
        if (userNo == null) { result.put("res", "login_required"); return result; }
        dto.setReporterId(userNo);
        service.submitReport(dto);
        result.put("res", "success");
        return result;
    }

}	