<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/point.css">

<div class="point-container">
    <div class="enhance-card">
        <div class="enhance-header">
            <h2>🔨 Forge of Titles (승급 강화소)</h2>
            <div class="current-balance">보유: <strong>${myPoint} P</strong></div>
        </div>

        <div class="enhance-body">
            <%-- 1. 동적으로 변하는 목표 수치 계산 --%>
            <c:set var="tier" value="${userTitle.tierOrder}" />
            <c:choose>
                <c:when test="${tier <= 2}"><c:set var="target" value="3" /><c:set var="pity" value="15" /></c:when>
                <c:when test="${tier <= 5}"><c:set var="target" value="4" /><c:set var="pity" value="20" /></c:when>
                <c:when test="${tier <= 8}"><c:set var="target" value="5" /><c:set var="pity" value="30" /></c:when>
                <c:otherwise><c:set var="target" value="7" /><c:set var="pity" value="50" /></c:otherwise>
            </c:choose>

            <%-- 2. 티어 표시 정보 --%>
            <div class="tier-display-box">
                <div class="tier-sub-label">GOAL: RANK ${target}</div>
                <div class="tier-main-name">${userTitle.titleName}</div>
                <div class="tier-level-indicator">현재 RANK ${userTitle.enhanceLevel}</div>
            </div>

            <%-- 3. 천장(Pity) 시스템 영역 --%>
            <div class="pity-section">
                <div class="pity-header">
                    <span>승급 보정 (Pity System)</span>
                    <span class="pity-count">${userTitle.failCount} / ${pity}</span>
                </div>
                <div class="pity-bar-bg">
                    <%-- 실패 횟수에 따른 게이지바 너비 계산 --%>
                    <div class="pity-bar-fill" style="width: ${(userTitle.failCount / pity) * 100}%"></div>
                </div>
                <small class="pity-notice">* ${pity}회 실패 시 다음 단계로 강제 승급합니다.</small>
            </div>

            <%-- 4. 강화 액션 영역 (이 부분이 빠져있었습니다!) --%>
            <div class="enhance-action" style="border-top: 1px solid #d0d7de; padding-top: 24px; margin-top: 20px;">
                <div class="enhance-cost" style="margin-bottom: 15px; color: #57606a;">
                    강화 비용: <strong style="color: #24292f;">100 P</strong>
                </div>
                <%-- 드디어 나타난 강화 버튼 --%>
                <button class="btn-promote-execute" onclick="doEnhance()" 
                        style="width: 100%; padding: 15px; font-size: 18px; font-weight: bold; cursor: pointer;">
                    승급 강화 시도
                </button>
            </div>
        </div>
    </div>
</div>

<script>
function doEnhance() {
    if(!confirm("100P를 사용하여 강화를 시도하시겠습니까?")) return;
    
    fetch('/point/enhanceTitle', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({titleId: ${userTitle.titleId}})
    })
    .then(res => res.json())
    .then(data => {
        // 성공/실패 여부와 관계없이 서버가 보내준 메시지 출력
        alert(data.message);
        
        // 포인트 부족 등의 사유가 아니라면 화면을 갱신하여 결과 반영
        if (data.res !== "fail" || data.message.includes("실패")) {
            location.reload(); 
        }
    })
    .catch(err => {
        console.error("Error:", err);
        alert("통신 중 오류가 발생했습니다.");
    });
}
</script>

<%-- 5. 푸터 누락 방지 --%>
<c:import url="/footer" />