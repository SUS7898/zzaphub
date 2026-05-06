package com.care.boot.users;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsersController {
	@Autowired private UsersService service;
	@Autowired private HttpSession session;
	@Autowired private KakaoService kakaoService; // 하단에 있던 Autowired를 위로 모았습니다.
	
	@RequestMapping("regist")
	public String regist() {
		return "users/regist";
	}
	
	@PostMapping("registProc")
	public String registProc(UsersDTO users, Model model, RedirectAttributes ra) {
		/* 기존 주석 유지 */
//		if(users.getAddress() != null && users.getAddress().trim().isEmpty() == false)
//			users.setAddress( postcode + "," + users.getAddress() + "," + detailAddress);
//		
		String msg = service.registProc(users);
		
		if(msg.equals("회원 등록 완료")) {
			ra.addFlashAttribute("msg", msg);
			return "redirect:index";
//			return "index";
		}
		
		model.addAttribute("msg", msg);
		return "users/regist";
	}
	
	@RequestMapping("login")
	public String login() {
		return "users/login";
	}
	
	@PostMapping("loginProc")
	public String loginProc(@RequestParam("loginId") String loginId, 
	                        @RequestParam("pw") String pw, 
	                        Model model, RedirectAttributes ra) {
		String msg = service.loginProc(loginId, pw);
		if(msg.equals("로그인 성공")) {
			ra.addFlashAttribute("msg", msg);
			return "redirect:index";
		}
		model.addAttribute("msg", msg);
		return "users/login";
	}
	
	@RequestMapping("logout")
	public String logout(RedirectAttributes ra) {
		session.invalidate();
		ra.addFlashAttribute("msg", "로그 아웃");
		
		// 카카오 로그아웃 연동
		kakaoService.unlink();
		return "redirect:index";
	}
	
	// 1. 전체 회원 목록 (관리자 전용)
	@RequestMapping("usersInfo") // 👈 (수정) userInfo가 아니라 usersInfo로 변경!
	public String usersInfo(@RequestParam(value="select", required=false) String select, 
	                        @RequestParam(value="search", required=false) String search,
	                        @RequestParam(value="currentPage", required=false) String cp, 
	                        Model model, RedirectAttributes ra) {
	                        
	    // 서비스에서 관리자 권한을 체크하고 결과를 받아옵니다. (서비스 메서드명도 usersInfo로 호출)
	    String msg = service.usersInfo(select, search, cp, model);
	    
	    if(msg.equals("회원 목록 조회 성공")) {
	        return "users/usersInfo"; // 관리자면 정상적으로 목록 페이지 이동
	    }
	    
	    // 관리자가 아니거나 로그인을 안 했으면 메인으로 튕겨냅니다.
	    ra.addFlashAttribute("msg", msg);
	    return "redirect:index";
	}

	// 2. 개별 회원 상세 정보 (본인 or 관리자)
	@RequestMapping("userInfo") 
	public String userInfo(@RequestParam("loginId") String loginId, 
	                       Model model, RedirectAttributes ra) {
	    String msg = service.userInfo(loginId, model);
	    loginId = (String)session.getAttribute("id");
	    
	    if(msg.equals("회원 검색 완료")) {
	        return "users/userInfo";
	    }
	    
	    // 권한이 없으면 돌려보냄
	    ra.addFlashAttribute("msg", msg);
	    return "redirect:usersInfo"; // 혹은 "redirect:index" 로 변경하셔도 됩니다.
	}
	
	//http://localhost:8086/dbQuiz/update
	@RequestMapping("update")
	public String update() {
		String sessionId = (String)session.getAttribute("id");
		if(sessionId == null)
			return "redirect:login";
		
		return "users/update";
	}
	
	@PostMapping("updateProc")
	public String updateProc(UsersDTO users, Model model) {
		String sessionId = (String)session.getAttribute("id");
		if(sessionId == null)
			return "redirect:login";
		
		users.setLoginId(sessionId);
		String msg = service.updateProc(users);
		if(msg.equals("회원 수정 완료")) {
			session.invalidate();
			return "redirect:index";
		}
		
		model.addAttribute("msg", msg);
		return "users/update";
	}
	
	//http://localhost:8086/dbQuiz/delete
	@RequestMapping("delete")
	public String delete() {
		String sessionId = (String)session.getAttribute("id");
		if(sessionId == null)
			return "redirect:login";
		
		return "users/delete";
	}
	
	@PostMapping("deleteProc")
	public String deleteProc(UsersDTO users, Model model) {
		String sessionId = (String)session.getAttribute("id");
		if(sessionId == null)
			return "redirect:login";
		
		users.setLoginId(sessionId);
		String msg = service.deleteProc(users);
		if(msg.equals("회원 삭제 완료")) {
			session.invalidate();
			return "redirect:index";
		}
		
		model.addAttribute("msg", msg);
		return "users/delete";
	}
	
	/*
	 http://localhost:8086/dbQuiz/kakaoLogin?
	 code=G2QFgIqYioKud_fa02jp1mikcoWU6ccLmKC_-T0xgHFoZlqddz74QKyM9sowSyG0x1c
	 xjwo9c00AAAGLA55NoQ
	 */
	@RequestMapping("kakaoLogin")
	public String kakaoLogin(@RequestParam("code") String code) {
		System.out.println("code : " + code);
		kakaoService.getAccessToken(code);
		kakaoService.getUserInfo();
		
		return "redirect:index";
	}
}