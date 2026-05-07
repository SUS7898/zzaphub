package com.care.boot.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private AdminService adminService;

    // 권한 체크 공통 메서드
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equals(role) || "MANAGER".equals(role);
    }

    // 유저 관리 페이지
    @GetMapping("/users")
    public String userManagement(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/"; // 권한 없으면 홈으로
        model.addAttribute("userList", adminService.getUserList());
        return "admin/users";
    }

    // 권한 변경 처리
    @PostMapping("/updateRole")
    public String updateRole(@RequestParam("userId") Integer userId, 
                             @RequestParam("newRole") String newRole, HttpSession session) {
      
    	
    	if (!"ADMIN".equals(session.getAttribute("role"))) return "redirect:/"; // 최고관리자만 권한변경 가능
        adminService.updateUserRole(userId, newRole);
        return "redirect:/admin/users";
    }

    // 신고 관리 페이지
    @GetMapping("/reports")
    public String reportManagement(@RequestParam(value="minCount", defaultValue="1") int minCount, 
                                   HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("reportSummary", adminService.getReportSummary(minCount));
        model.addAttribute("minCount", minCount);
        return "admin/reports";
    }

    // 신고 해결 완료 처리
    @GetMapping("/reportResolve")
    public String reportResolve(@RequestParam("id") Integer targetId, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/";
        adminService.resolveReport(targetId);
        return "redirect:/admin/reports";
    }
    
 // 유저 상세 페이지 이동
    @GetMapping("/userDetail")
    public String userDetail(@RequestParam("id") Integer id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("targetUser", adminService.getUserById(id));
        return "admin/userDetail";
    }

    // 포인트 업데이트 처리
    @PostMapping("/updatePoint")
    public String updatePoint(@RequestParam("userId") Integer userId, 
                               @RequestParam("amount") Integer amount, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/";
        adminService.updateUserPoint(userId, amount);
        return "redirect:/admin/userDetail?id=" + userId;
    }
 // 🚫 회원 추방(제재) 처리
 // JSP에서 "/admin/userBan"으로 호출하므로, 클래스 레벨의 /admin과 합쳐져 주소가 완성됩니다.
    @RequestMapping("/userBan")
    public String userBan(@RequestParam("id") String id, HttpSession session, RedirectAttributes ra) {
        // 🛡️ 보안 체크 추가: 관리자/매니저가 아니면 홈으로 튕겨냄
        if (!isAdmin(session)) {
            ra.addFlashAttribute("msg", "권한이 없습니다.");
            return "redirect:/";
        }
        System.out.println("======= 회원 추방 진입: " + id + " =======");
        
        String msg = adminService.userBan(id); // 서비스에서 BANNED 처리
        ra.addFlashAttribute("msg", msg);
        
        // 💡 중요: 처리가 끝나면 반드시 목록으로 '리다이렉트' 해야 404를 피할 수 있습니다.
        return "redirect:/admin/users"; 
    }
}