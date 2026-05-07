package com.care.boot.admin;

import com.care.boot.users.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired private IAdminMapper adminMapper;

    public List<UsersDTO> getUserList() {
        return adminMapper.getUserList();
    }

    public void updateUserRole(Integer userId, String newRole) {
        adminMapper.updateUserRole(userId, newRole);
    }

    public List<Map<String, Object>> getReportSummary(int minCount) {
        return adminMapper.getReportSummary(minCount);
    }

    public void resolveReport(Integer targetId) {
        adminMapper.updateReportStatus(targetId, "처리완료");
    }
    public UsersDTO getUserById(Integer id) {
        return adminMapper.getUserById(id);
    }

    
    public void updateUserPoint(Integer userId, Integer amount) {
        adminMapper.updateUserPoint(userId, amount);
    }
    public String userBan(String id) {
        try {
            int userId = Integer.parseInt(id);
            
            // 이미 만들어두신 updateUserRole 메서드를 활용합니다.
            adminMapper.updateUserRole(userId, "BANNED");
            
            return "해당 회원이 강제 탈퇴(정지) 처리되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "회원 처리 중 오류가 발생했습니다.";
        }
    }
}