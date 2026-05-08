package com.care.boot.point;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import com.care.boot.users.IUserMapper;
import com.care.boot.users.UsersDTO;

@Controller
@RequestMapping("/point")
public class PointController {
    
    @Autowired private PointService pointService;
    @Autowired private IPointMapper pointMapper; // ✅ 인벤토리 조회를 위해 매퍼 주입
    @Autowired private IUserMapper userMapper;

    // 공통 메서드: 세션의 문자열 ID로 DB의 고유 PK(Long userId)를 찾습니다.
    private Long getUserId(HttpSession session) {
        String loginId = (String) session.getAttribute("id");
        if (loginId == null) return null;
        UsersDTO user = userMapper.login(loginId);
        return user != null ? user.getId() : null;
    }

    // ==========================================
    // 🖥️ 화면 이동 매핑 (View)
    // ==========================================
    @GetMapping("/shop")
    public String shop(Model model, HttpSession session) {
        if(getUserId(session) == null) return "redirect:/login";
        return "point/shop"; 
    }

    @GetMapping("/enhanceView")
    public String enhanceView(@RequestParam(value = "titleId", required = false) Integer titleId, 
                              Model model, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";
        
        // 🌟 titleId가 없으면 강화할 대상을 선택하도록 인벤토리로 보냅니다.
        if (titleId == null) {
            return "redirect:/point/inventory";
        }
        
        UserTitleDTO ut = pointMapper.getUserTitle(userId, titleId);
        if (ut == null) return "redirect:/point/inventory";
     // 🛡️ 가챠 칭호인데 강화를 시도하려고 하면 인벤토리로 쫓아냄
        if (ut == null || "GACHA".equals(ut.getTitleType())) {
            return "redirect:/point/inventory";
        }
        model.addAttribute("userTitle", ut);
        model.addAttribute("myPoint", pointMapper.getUserPoint(userId));
        return "point/enhance"; 
    }

    // ✅ 오류 해결된 인벤토리 매핑
    @GetMapping("/inventory")
    public String inventory(Model model, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";
        
        // 인터페이스 직접 호출(IPointMapper.getMyTitles) 오류 수정 -> 의존성 주입된 인스턴스 사용
        model.addAttribute("myTitles", pointMapper.getMyTitles(userId));
        return "point/inventory"; 
    }
    
    // ==========================================
    // ⚙️ 데이터 처리 API 매핑 (AJAX)
    // ==========================================
    @PostMapping("/unlockBeginner")
    @ResponseBody
    public PointResultDTO unlockBeginner(HttpSession session) {
        Long userId = getUserId(session);
        if(userId == null) return PointResultDTO.fail("로그인이 필요합니다.");
        return pointService.unlockBeginner(userId);
    }

    @PostMapping("/drawGacha")
    @ResponseBody
    public PointResultDTO drawGacha(HttpSession session) {
        Long userId = getUserId(session);
        if(userId == null) return PointResultDTO.fail("로그인이 필요합니다.");
        return pointService.drawGacha(userId);
    }

    @PostMapping("/buyEmoticon")
    @ResponseBody 
    public PointResultDTO buyEmoticon(@RequestBody Map<String, Integer> req, HttpSession session) {
        Long userId = getUserId(session);
        if(userId == null) return PointResultDTO.fail("로그인이 필요합니다.");
        return pointService.buyEmoticon(userId, req.get("emoticonId"));
    }

    @PostMapping("/buyTitle")
    @ResponseBody 
    public PointResultDTO buyTitle(@RequestBody Map<String, Integer> req, HttpSession session) {
        Long userId = getUserId(session);
        if(userId == null) return PointResultDTO.fail("로그인이 필요합니다.");
        return pointService.buyTitle(userId, req.get("titleId"));
    }

    // ✅ 오타 수정 완료 (getUser\nd -> getUserId)
    @PostMapping("/enhanceTitle")
    @ResponseBody 
    public PointResultDTO enhanceTitle(@RequestBody Map<String, Integer> req, HttpSession session) {
        Long userId = getUserId(session); 
        if(userId == null) return PointResultDTO.fail("로그인이 필요합니다.");
        return pointService.enhanceTitle(userId, req.get("titleId"));
    }
    
    @PostMapping("/equipTitle")
    @ResponseBody
    public PointResultDTO equipTitle(@RequestBody Map<String, Integer> req, HttpSession session) {
        Long userId = getUserId(session);
        if(userId == null) return PointResultDTO.fail("로그인이 필요합니다.");
        return pointService.equipTitle(userId, req.get("titleId"));
    }
}