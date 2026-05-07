package com.care.boot.posts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    @Autowired private IPostsMapper mapper;
    @Autowired private IUserMapper userMapper;
    @Autowired private HttpSession session;
    
    // 원래 사용하시던 경로 그대로 복구 (NFS 마운트가 OS단에서 이 경로로 연결된다고 가정)
    private String filePath = "/opt/tomcat/tomcat-10/webapps/upload/";
    
    public void postsForm(String cp, String category, Model model) {
        int currentPage = 1;
        try { currentPage = Integer.parseInt(cp); } catch(Exception e) { currentPage = 1; }

        int pageBlock = 10;
        int begin = (currentPage - 1) * pageBlock;

        List<PostsDTO> postsList = mapper.getPostsList(category, begin, pageBlock);
        int totalCount = mapper.getTotalCountByCategory(category);

        String url = "postsForm?category=" + category + "&currentPage=";
        String result = PageService.printPage(url, totalCount, pageBlock, currentPage);

        model.addAttribute("postsList", postsList);
        model.addAttribute("totalPosts", totalCount);
        model.addAttribute("result", result);
    }

    public List<PostsDTO> getPostsByCategory(String category, int limit) {
        return mapper.getRecentByCategory(category, limit);
    }

    public List<PostsDTO> getPopularPosts(int limit) {
        return mapper.getPopularPosts(limit);
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
                posts.setFileName(fullPath); // DB에 파일 전체 경로 저장 (프리뷰를 위해 필수)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        mapper.postsWriteProc(posts);
        userMapper.addPoint(user.getId(), 15);
        
        return "redirect:postsForm";
    }
    
    public PostsDTO postsContent(String id) {
        long n = 0;
        try { n = Long.parseLong(id); } catch(Exception e) { return null; }
        
        PostsDTO posts = mapper.postsContent(n);
        if(posts != null) {
            mapper.incrementHits(n);
            posts.setViewCount(posts.getViewCount() + 1);
        }
        return posts;
    }

    // 🔍 코드 프리뷰 기능 유지
    public String getFilePreviewContent(String fullPath) {
        if (fullPath == null || fullPath.isEmpty()) return null;
        
        String lowerPath = fullPath.toLowerCase();
        // .class 제외, 코딩 확장자만 필터링
        if (lowerPath.endsWith(".txt") || lowerPath.endsWith(".js") || 
            lowerPath.endsWith(".py") || lowerPath.endsWith(".java") || 
            lowerPath.endsWith(".html") || lowerPath.endsWith(".xml")) {
            
            File f = new File(fullPath);
            if (!f.exists()) return null;
            
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(fullPath));
                String content = new String(bytes, StandardCharsets.UTF_8);
                return content.length() > 2000 ? content.substring(0, 2000) + "\n\n... (파일 내용이 길어 생략되었습니다)" : content;
            } catch (Exception e) {
                return "파일 내용을 읽는 중 오류가 발생했습니다.";
            }
        }
        return null;
    }

    public void postsDownload(String id, HttpServletResponse response) {
        long n = 0;
        try { n = Long.parseLong(id); } catch(Exception e) { return; }
        
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
        try { n = Long.parseLong(id); } catch (Exception e) { return "redirect:postsForm"; }
        
        PostsDTO posts = mapper.postsContent(n);
        if(posts == null) return "redirect:postsForm";
        
        model.addAttribute("posts", posts);
        return "posts/postsModify";
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

    public String postsDeleteProc(String id, String role) {
        long n = 0;
        try { n = Long.parseLong(id); } catch (Exception e) { return "게시글 번호 문제"; }
        
        PostsDTO posts = mapper.postsContent(n);
        if(posts == null) return "게시글이 존재하지 않습니다.";
        
        String sessionId = (String)session.getAttribute("id");
        boolean isAdmin = "ADMIN".equals(role) || "MANAGER".equals(role);
        boolean isOwner = sessionId != null && sessionId.equals(posts.getLoginId());

        if(isAdmin || isOwner) {
            mapper.deleteCommentsByPostId(n);
            mapper.postsDeleteProc(n);
            return "게시글 삭제 완료";
        }
        return "작성자만 삭제 할 수 있습니다.";
    }
}