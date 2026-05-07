package com.care.boot.users;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.care.boot.PageService;

import jakarta.servlet.http.HttpSession;

@Service
public class UsersService {
	@Autowired private IUserMapper mapper;
	@Autowired private HttpSession session;
	
	@Transactional
	public String registProc(UsersDTO user) {
		if(user.getLoginId() == null || user.getLoginId().trim().isEmpty()) {
			return "아이디를 입력하세요.";
		}
		if(user.getPw() == null || user.getPw().trim().isEmpty()) {
			return "비밀번호를 입력하세요.";
		}
		if(user.getPw().equals(user.getConfirm()) == false) {
			return "두 비밀번호를 일치하여 입력하세요.";
		}
		if(user.getName() == null || user.getName().trim().isEmpty()) {
			return "이름을 입력하세요.";
		}
		
		UsersDTO check = mapper.login(user.getLoginId());
		if(check != null) {
			return "이미 사용중인 아이디 입니다.";
		}
		
		/* 암호화 과정 */
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String secretPass = encoder.encode(user.getPw());
		user.setPw(secretPass);
		
		int result = mapper.registProc(user);
		if(result == 1)
			return "회원 등록 완료";
		
		return "회원 등록을 다시 시도하세요.";
	}
	
	public String loginProc(String loginId, String pw) {
	    if(loginId == null || loginId.trim().isEmpty()) {
	        return "아이디를 입력하세요.";
	    }
	    if(pw == null || pw.trim().isEmpty()) {
	        return "비밀번호를 입력하세요.";
	    }
	    
	    UsersDTO check = mapper.login(loginId);
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    
	    if(check != null && encoder.matches(pw, check.getPw())) {
	        if("BANNED".equals(check.getRole())) {
	            return "정지된 계정입니다. 관리자에게 문의하세요.";
	        }
	        
	        session.setAttribute("id", check.getLoginId());
	        session.setAttribute("userName", check.getName());
	        session.setAttribute("userNo", check.getId());
	        session.setAttribute("role", check.getRole());
	        
	        return "로그인 성공";
	    }
	    
	    return "아이디 또는 비밀번호를 확인 후 다시 입력하세요.";
	}

	@Transactional(readOnly = true)
	public String usersInfo(String select, String search, String cp, Model model) {
	    String sessionId = (String) session.getAttribute("id");
	    if (sessionId == null) {
	        return "로그인 후 이용하세요.";
	    }
	    if (sessionId.equals("admin") == false) {
	        return "관리자만 접근 가능한 메뉴입니다.";
	    }
	    
	    int currentPage = 1;
	    try {
	        currentPage = Integer.parseInt(cp);
	    } catch(Exception e) {
	        currentPage = 1;
	    }
	    
	    if (select == null) select = "";
	    
	    int pageBlock = 3; 
	    int end = pageBlock * currentPage; 
	    int begin = end - pageBlock + 1; 
	    
	    ArrayList<UsersDTO> users = mapper.userInfo(begin, end, select, search);
	    int totalCount = mapper.totalCount(select, search);
	    
	    if (totalCount == 0) {
	        return "회원 목록 조회 성공";
	    }
	    
	    String url = "usersInfo?select="+select+"&search="+search+"&currentPage=";
	    String result = PageService.printPage(url, totalCount, pageBlock, currentPage);
	    
	    model.addAttribute("select", select);
	    model.addAttribute("search", search);
	    model.addAttribute("result", result);
	    model.addAttribute("users", users);
	    
	    return "회원 목록 조회 성공";
	}
	
	@Transactional(readOnly = true)
	public String userInfo(String loginId, Model model) {
		String sessionId = (String)session.getAttribute("id");
		if(sessionId == null)
			return "로그인 후 이용하세요.";
		
		if(sessionId.equals("admin") == false && sessionId.equals(loginId) == false) {
			return "본인의 아이디를 선택하세요.";
		}
		
		UsersDTO user = mapper.login(loginId);
		model.addAttribute("user", user);
		return "회원 검색 완료";
	}

	@Transactional
	public String updateProc(UsersDTO user) {
		if(user.getPw() == null || user.getPw().trim().isEmpty()) {
			return "비밀번호를 입력하세요.";
		}
		if(user.getPw().equals(user.getConfirm()) == false) {
			return "두 비밀번호를 일치하여 입력하세요.";
		}
		if(user.getName() == null || user.getName().trim().isEmpty()) {
			return "이름을 입력하세요.";
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String secretPass = encoder.encode(user.getPw());
		user.setPw(secretPass);
		
		int result = mapper.updateProc(user);
		if(result == 1)
			return "회원 수정 완료";
		
		return "회원 수정을 다시 시도하세요.";
	}

	@Transactional
	public String deleteProc(UsersDTO user) {
		if(user.getPw() == null || user.getPw().trim().isEmpty()) {
			return "비밀번호를 입력하세요.";
		}
		if(user.getPw().equals(user.getConfirm()) == false) {
			return "두 비밀번호를 일치하여 입력하세요.";
		}
		
		UsersDTO check = mapper.login(user.getLoginId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(check != null && encoder.matches(user.getPw(), check.getPw()) == true) {
			int result = mapper.deleteProc(user.getLoginId());
			if(result == 1)
				return "회원 삭제 완료";
			return "회원 삭제를 다시 시도하세요.";
		}
		
		return "아이디 또는 비밀번호를 확인 후 입력하세요";
	}
	
	// UsersService.java 에 아래 메서드들을 추가하거나 수정하세요.

	// 🔍 아이디 찾기: 이메일로 검색하여 아이디만 반환
	public String findIdByEmail(String email) {
	    return mapper.findIdByEmail(email);
	}

	// 🔐 비밀번호 업데이트 (resetPwProc 내부에서 사용)
	@Transactional
	public String resetPwProc(String loginId, String pw, String pwConfirm) {
	    if(pw == null || pw.trim().isEmpty()) return "새 비밀번호를 입력하세요.";
	    if(!pw.equals(pwConfirm)) return "비밀번호가 일치하지 않습니다.";
	    
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    String encodedPw = encoder.encode(pw);
	    
	    // mapper에 updatePw 메서드가 있어야 합니다.
	    int result = mapper.updatePw(loginId, encodedPw);
	    return result > 0 ? "success" : "fail";
	}

}