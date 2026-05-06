package com.care.boot.posts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.care.boot.PageService;
import com.care.boot.users.IUserMapper;
import com.care.boot.users.UsersDTO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class PostsService {
    @Autowired private IPostsMapper mapper; // 인터페이스 이름 PostsMapper로 수정
    @Autowired private IUserMapper userMapper;
    @Autowired private HttpSession session;
    
    private String filePath = "/opt/tomcat/tomcat-10/webapps/upload/";
    
    public void postsForm(String cp, Model model) {
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(cp);
        } catch(Exception e) {
            currentPage = 1;
        }
        
        int pageBlock = 3; 
        int begin = (currentPage - 1) * pageBlock; 
        
        // Mapper의 매개변수명과 일치하게 전달
        List<PostsDTO> postss = mapper.postsForm(begin, pageBlock);
        int totalCount = mapper.totalCount();
        
        if(totalCount == 0) return;
        
        String url = "postsForm?currentPage=";
        String result = PageService.printPage(url, totalCount, pageBlock, currentPage);
        
        model.addAttribute("postsList", postss);
        model.addAttribute("result", result);
    }
    
    public String postsWriteProc(MultipartHttpServletRequest multi) {
        String sessionId = (String) session.getAttribute("id");
        if(sessionId == null) return "redirect:login";
        
        UsersDTO user = userMapper.login(sessionId);
        if(user == null) return "redirect:login";
        
        String title = multi.getParameter("title");
        if(title == null || title.trim().isEmpty()) {
            return "redirect:postsWrite";
        }
        
        PostsDTO posts = new PostsDTO();
        posts.setTitle(title);
        posts.setContent(multi.getParameter("content"));
        posts.setUserId(user.getId()); 
        posts.setCategory(multi.getParameter("category"));
        
        // 파일 업로드 로직
        MultipartFile file = multi.getFile("upfile");
        if(file != null && file.getSize() != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss-");
            String fileTime = sdf.format(new Date());
            String fileName = file.getOriginalFilename();
            
            String fileSaveDirectory = filePath + sessionId;
            File f = new File(fileSaveDirectory);
            if(!f.exists()) f.mkdirs();
            
            String fullPath = fileSaveDirectory + "/" + fileTime + fileName;
            try {
                file.transferTo(new File(fullPath));
                // posts.setFileName(fullPath); // DTO에 필드가 있다면 주석 해제
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        mapper.postsWriteProc(posts);
        return "redirect:postsForm";
    }
    
    public PostsDTO postsContent(String id) {
        long n = 0;
        try {
            n = Long.parseLong(id);
        } catch(Exception e) {
            return null;
        }
        
        PostsDTO posts = mapper.postsContent(n);
        if(posts != null) {
            mapper.incrementHits(n); // Mapper 메서드명 incrementHits로 수정
            posts.setViewCount(posts.getViewCount() + 1);
        }
        return posts;
    }

    public void postsDownload(String id, HttpServletResponse response) {
        long n = 0;
        try {
            n = Long.parseLong(id);
        } catch(Exception e) {
            return;
        }
        
        String fullPath = mapper.postsDownload((int)n); 
        if(fullPath == null || fullPath.isEmpty()) return;
        
        try {
            File file = new File(fullPath);
            if(!file.exists()) return;
            
            String fileName = fullPath.substring(fullPath.lastIndexOf("/") + 1).split("-", 2)[1];
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            
            FileInputStream fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, response.getOutputStream());
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String postsModify(String id, Model model) {
        long n = 0;
        try {
            n = Long.parseLong(id);
        } catch (Exception e) {
            return "redirect:postsForm";
        }
        
        PostsDTO posts = mapper.postsContent(n);
        if(posts == null) return "redirect:postsForm";
        
        model.addAttribute("posts", posts);
        return "posts/postsModify"; // JSP 폴더 posts/
    }

    public String postsModifyProc(PostsDTO posts) {
        PostsDTO check = mapper.postsContent(posts.getId());
        if(check == null) return "게시글 번호에 문제가 발생했습니다.";
        
        String sessionId = (String)session.getAttribute("id");
        UsersDTO user = userMapper.login(sessionId);
        
        if(user == null || !check.getUserId().equals(user.getId()))
            return "작성자만 수정 할 수 있습니다.";
        
        if(posts.getTitle() == null || posts.getTitle().trim().isEmpty()) {
            return "제목을 입력하세요.";
        }
        
        int result = mapper.postsModifyProc(posts);
        if(result == 0) return "게시글 수정 실패";
        
        return "게시글 수정 성공";
    }

    public String postsDeleteProc(String id) {
        long n = 0;
        try {
            n = Long.parseLong(id);
        } catch (Exception e) {
            return "게시글 번호 문제";
        }
        
        PostsDTO posts = mapper.postsContent(n);
        if(posts == null) return "게시글이 존재하지 않습니다.";
        
        String sessionId = (String)session.getAttribute("id");
        UsersDTO user = userMapper.login(sessionId);
        
        if(user == null || !posts.getUserId().equals(user.getId()))
            return "작성자만 삭제 할 수 있습니다.";
        
        mapper.postsDeleteProc(n);
        return "게시글 삭제 완료";
    }
}