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
		/*
			암호문 : $2a$10$HJ3CfbI4MxDDSM3emVsuNudQyQE5StjV7g/UGK2vSQZQRmGy23OXi
			암호문 길이: 60
			
			암호문 : $2a$10$nGmxZK6PVs.NV.QY.UX2T.OuGprkSwMs7FrNq6sOi1RfFPflQWUmO
			암호문 길이: 60
			
			pw 컬럼의 크기를 암호문 크기와 같거나 크게 변경
			ALTER TABLE db_quiz MODIFY pw varchar2(60);
			COMMIT;
		 */
		System.out.println("암호문 : " + secretPass);
		System.out.println("암호문 길이: " + secretPass.length());
		
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
		if(check != null && encoder.matches(pw, check.getPw()) == true) {
			session.setAttribute("id", check.getLoginId());
			session.setAttribute("userName", check.getName());
			// 새 DB에는 address 대신 phone과 email이 있습니다. 필요시 주석 해제하여 사용하세요.
			// session.setAttribute("phone", check.getPhone());
			// session.setAttribute("email", check.getEmail());
			
			
			/*
			 * session.setAttribute("user", check);
			 * ${sessionScope.user.id}
			 * ${sessionScope.user.pw}
			 * ${sessionScope.user.userName}
			 */
			return "로그인 성공";
		}
		
		return "아이디 또는 비밀번호를 확인 후 다시 입력하세요.";
	}
	@Transactional(readOnly = true)
	public String usersInfo(String select, String search, String cp, Model model) {
	    // 1. 세션 확인 및 관리자 권한 체크
	    String sessionId = (String) session.getAttribute("id");
	    if (sessionId == null) {
	        return "로그인 후 이용하세요.";
	    }
	    if (sessionId.equals("admin") == false) {
	        return "관리자만 접근 가능한 메뉴입니다.";
	    }
	    
	    // 2. 기존 페이징 로직 실행
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
	        return "회원 목록 조회 성공"; // 데이터가 없어도 페이지는 띄워야 하므로 성공 리턴
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
		/* [수정 포인트] 
		   새로운 DB(users)에는 address, detailAddress 컬럼이 없습니다.
		   따라서 기존의 콤마(,)로 주소를 쪼개는 로직은 일단 주석 처리합니다.
		   만약 나중에 주소 기능이 필요해지면 DB에 컬럼을 추가하고 아래 코드를 살리시면 됩니다.
		*/
		/*
		if(user.getAddress() != null && user.getAddress().isEmpty() == false) {
			String[] address = user.getAddress().split(",");
			if(address.length >= 2) {
				model.addAttribute("postcode", address[0]);
				user.setAddress(address[1]);
				if(address.length == 3) {
					model.addAttribute("detailAddress", address[2]);
				}
			}
		}
		*/
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
		/* 암호화 과정 */
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

}

















