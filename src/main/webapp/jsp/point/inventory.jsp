<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/point.css">

<div class="point-container" style="max-width: 900px; margin: 40px auto; padding: 0 20px;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <h2 style="font-size: 24px; font-weight: 600; margin: 0;">📦 나의 인벤토리</h2>
        <span style="color: #57606a; font-size: 14px;">보유 중인 칭호를 관리하고 장착하세요.</span>
    </div>
    
    <div class="gh-card">
        <div class="gh-card-header" style="background-color: #f6f8fa; border-bottom: 1px solid #d0d7de; padding: 16px;">
            <strong>보유 칭호 목록</strong>
        </div>
        
        <div class="gh-card-body" style="padding: 24px;">
            <div class="inventory-grid" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 20px;">
                
                <c:forEach var="t" items="${myTitles}">
                    <%-- 장착 중인 아이템은 강조 스타일 적용 --%>
                    <div class="item-card ${t.isEquipped ? 'equipped' : ''}" 
                         style="border: 1px solid ${t.isEquipped ? '#0969da' : '#d0d7de'}; 
                                border-radius: 8px; padding: 20px; text-align: center; 
                                background: ${t.isEquipped ? '#f0faff' : '#fff'};
                                transition: transform 0.2s, box-shadow 0.2s;">
                        
                        <%-- 칭호 이름 및 강화 수치 --%>
                        <div class="item-name" style="font-weight: 700; font-size: 18px; color: #24292f; margin-bottom: 12px;">
                            ${t.titleName}
                            <c:if test="${t.enhanceLevel > 0}">
                                <span style="color: #0969da; margin-left: 4px;">+${t.enhanceLevel}</span>
                            </c:if>
                        </div>

                        <%-- 버튼 그룹 영역 --%>
                        <div class="button-group" style="display: flex; flex-direction: column; gap: 8px;">
                            
                            <%-- 1. 장착/미장착 상태 표시 --%>
                            <c:choose>
                                <c:when test="${t.isEquipped}">
                                    <div style="background: #2da44e; color: white; padding: 6px; border-radius: 6px; font-size: 13px; font-weight: 600;">
                                        ✅ 현재 장착 중
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn-promote-execute" onclick="equipTitle(${t.titleId})" 
                                            style="background: #fff; color: #24292f; border: 1px solid #d0d7de; padding: 6px; font-size: 13px;">
                                        장착하기
                                    </button>
                                </c:otherwise>
                            </c:choose>
                            
                            <%-- 2. 🌟 승급형(PROGRESSION)일 때만 승급 강화 버튼 노출 --%>
                            <c:if test="${t.titleType eq 'PROGRESSION'}">
                                <button class="btn-promote-execute" 
                                        style="background: #24292f; color: #e3b341; border: 1px solid #e3b341; padding: 8px; font-size: 13px; font-weight: bold;"
                                        onclick="location.href='/point/enhanceView?titleId=${t.titleId}'">
                                    🔨 승급 강화소
                                </button>
                            </c:if>

                            <%-- 3. 가챠형(GACHA)일 때는 별도 안내 (선택 사항) --%>
                            <c:if test="${t.titleType eq 'GACHA'}">
                                <small style="color: #8c959f; font-size: 11px; margin-top: 5px;">수집 전용 리미티드 칭호</small>
                            </c:if>

                        </div>
                    </div>
                </c:forEach>
                
                <%-- 보유한 칭호가 하나도 없을 때 --%>
                <c:if test="${empty myTitles}">
                    <div style="grid-column: 1 / -1; text-align: center; padding: 50px; color: #57606a;">
                        <div style="font-size: 40px; margin-bottom: 10px;">📦</div>
                        <p>보유 중인 칭호가 없습니다.<br>아이템 상점에서 칭호를 획득해 보세요!</p>
                        <button class="btn-gh" onclick="location.href='/point/shop'" style="margin-top: 15px;">상점 바로가기</button>
                    </div>
                </c:if>

            </div>
        </div>
    </div>
</div>

<script>
/**
 * 칭호 장착 처리 함수
 */
function equipTitle(id) {
    if(!confirm("이 칭호를 장착하시겠습니까?")) return;

    fetch('/point/equipTitle', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({titleId: id})
    })
    .then(res => res.json())
    .then(data => {
        alert(data.message);
        if(data.success) {
            location.reload(); // 성공 시 페이지 새로고침하여 뱃지 반영
        }
    })
    .catch(err => {
        console.error("Equip Error:", err);
        alert("장착 처리 중 오류가 발생했습니다.");
    });
}
</script>

<c:import url="/footer" />