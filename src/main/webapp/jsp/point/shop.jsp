<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />
<link rel="stylesheet" href="/css/point.css">

<style>
/* 특별 배너 스타일 */
.milestone-banner { background: linear-gradient(135deg, #0969da, #2da44e); color: #fff; padding: 20px; border-radius: 6px; margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center; }
.gacha-card { border: 2px dashed #bf8700; background: #fff8c5; padding: 30px; text-align: center; border-radius: 6px; margin-bottom: 24px; }
.btn-gacha { background: #bf8700; color: #fff; border: none; padding: 10px 20px; font-size: 16px; border-radius: 6px; font-weight: bold; cursor: pointer; transition: 0.2s; }
.btn-gacha:hover { background: #9a6700; transform: scale(1.05); }
</style>

<div class="point-container">
    <div class="user-point-display">
        <span>나의 보유 포인트</span>
        <strong id="myPoint">${user.point} P</strong>
    </div>

    <div class="milestone-banner">
        <div>
            <h3 style="margin: 0 0 5px 0;">🚀 코더의 첫 걸음</h3>
            <span style="font-size: 14px; opacity: 0.9;">1000 포인트를 모았다면 'Beginner' 칭호를 무료로 해금하세요! (이후 승급 가능)</span>
        </div>
        <button class="btn-buy" style="width: auto; background: #fff; color: #0969da;" onclick="callApi('/point/unlockBeginner')">비기너 칭호 획득하기</button>
    </div>

    <div class="gacha-card">
        <h2 style="margin: 0 0 10px 0; color: #9a6700;">🎁 랜덤 칭호 뽑기 상자</h2>
        <p style="color: #6e7781; margin-bottom: 20px;">300P를 사용하여 특별한(코딩과 관련 없는) 예능 칭호를 뽑아보세요!</p>
        <button class="btn-gacha" onclick="callApi('/point/drawGacha')">300 P로 1회 뽑기</button>
    </div>

    <div class="gh-card">
        <div class="gh-card-header">일반 아이템 상점</div>
        <div class="gh-card-body shop-grid">
            <c:forEach var="item" items="${availableItems}">
                <div class="item-card">
                    <span class="item-name">${item.name}</span>
                    <span class="item-price">💰 ${item.price} P</span>
                    <button class="btn-buy" onclick="callApi('/point/buyItem', {itemId: ${item.id}})">구매하기</button>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<script>
function callApi(url, data = {}) {
    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(res => {
        // 만약 서버 에러(500)가 나면 JSON이 아니므로 여기서 걸러짐
        if(!res.ok) throw new Error("서버 응답 에러");
        return res.json();
    })
    .then(resData => {
        // resData.message 가 자바의 PointResultDTO.message와 매칭됨
        if (resData.message) {
            alert(resData.message);
        }
        if (resData.success) {
            location.reload();
        }
    })
    .catch(err => {
        console.error(err);
        alert("처리에 실패했습니다. (서버 연결 확인)");
    });
}</script>
<c:import url="/footer" />