package com.care.boot.point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

@Service
public class PointService {
    
    @Autowired private IPointMapper pointMapper;
    private final Random random = new Random();

    // 🛡️ 칭호 장착 로직
    @Transactional
    public PointResultDTO equipTitle(Long userId, Integer titleId) {
        pointMapper.unequipAll(userId); // 1. 기존 장착 칭호 모두 해제
        pointMapper.equipTitle(userId, titleId); // 2. 선택한 칭호 장착
        return PointResultDTO.ok("칭호를 성공적으로 장착했습니다.", 0);
    }
    
    // 🚀 1000포인트 달성 기념 무료 칭호 해금
    @Transactional
    public PointResultDTO unlockBeginner(Long userId) {
        int currentPoint = pointMapper.getUserPoint(userId);
        if (currentPoint < 1000) {
            return PointResultDTO.fail("포인트가 부족합니다. (누적 1,000P 이상 달성 시 해금 가능)");
        }

        // DB에서 'Beginner'라는 이름의 칭호 ID를 동적으로 탐색
        Integer beginnerId = pointMapper.getTitleIdByName("Beginner");
        if (beginnerId == null) return PointResultDTO.fail("DB에 'Beginner' 칭호가 존재하지 않습니다.");
        if (pointMapper.getUserTitle(userId, beginnerId) != null) return PointResultDTO.fail("이미 'Beginner' 칭호를 보유하고 있습니다.");

        pointMapper.insertUserTitle(userId, beginnerId);
        return PointResultDTO.ok("🎉 축하합니다! 1,000P 달성 기념 'Beginner' 칭호가 해금되었습니다.", currentPoint);
    }

    // 🎲 300P 랜덤 가챠 뽑기
    @Transactional
    public PointResultDTO drawGacha(Long userId) {
        int GACHA_COST = 300;
        int currentPoint = pointMapper.getUserPoint(userId);

        if (currentPoint < GACHA_COST) return PointResultDTO.fail("포인트가 부족합니다. (300P 필요)");

        UserTitleDTO randomTitle = pointMapper.getRandomGachaTitle(userId);
        if (randomTitle == null) return PointResultDTO.fail("모든 가챠 칭호를 다 모으셨습니다!");

        pointMapper.deductPoint(userId, GACHA_COST);
        pointMapper.insertUserTitle(userId, randomTitle.getTitleId());
        return PointResultDTO.ok("🎊 뽑기 성공! [" + randomTitle.getTitleName() + "] 칭호를 획득했습니다!", currentPoint - GACHA_COST);
    }

    // 🎁 상점 구매 (이모티콘, 일반 칭호)
    @Transactional
    public PointResultDTO buyEmoticon(Long userId, Integer emoticonId) {
        if(pointMapper.checkUserEmoticon(userId, emoticonId) > 0) return PointResultDTO.fail("이미 보유한 이모티콘입니다.");
        Integer price = pointMapper.getEmoticonPrice(emoticonId);
        if(price == null) return PointResultDTO.fail("존재하지 않는 상품입니다.");
        
        int currentPoint = pointMapper.getUserPoint(userId);
        if(currentPoint < price) return PointResultDTO.fail("포인트가 부족합니다.");
        
        pointMapper.deductPoint(userId, price);
        pointMapper.insertUserEmoticon(userId, emoticonId);
        return PointResultDTO.ok("이모티콘 구매 성공!", currentPoint - price);
    }

    @Transactional
    public PointResultDTO buyTitle(Long userId, Integer titleId) {
        if(pointMapper.getUserTitle(userId, titleId) != null) return PointResultDTO.fail("이미 보유한 칭호입니다.");
        UserTitleDTO titleInfo = pointMapper.getTitleInfo(titleId);
        if(titleInfo == null || titleInfo.getPrice() == null) return PointResultDTO.fail("구매할 수 없는 칭호입니다.");
        
        int currentPoint = pointMapper.getUserPoint(userId);
        if(currentPoint < titleInfo.getPrice()) return PointResultDTO.fail("포인트가 부족합니다.");
        
        pointMapper.deductPoint(userId, titleInfo.getPrice());
        pointMapper.insertUserTitle(userId, titleId);
        return PointResultDTO.ok("칭호 구매 성공!", currentPoint - titleInfo.getPrice());
    }

    // 🔨 칭호 확률 강화 및 15회 천장 시스템 로직
    @Transactional
    public PointResultDTO enhanceTitle(Long userId, Integer titleId) {
        int ENHANCE_COST = 100;
        int currentPoint = pointMapper.getUserPoint(userId);
        if(currentPoint < ENHANCE_COST) return PointResultDTO.fail("포인트가 부족합니다.");

        // 유저의 칭호 정보와 티어 순서(tierOrder)를 함께 가져옵니다.
        UserTitleDTO ut = pointMapper.getUserTitle(userId, titleId);
        if(ut == null || ut.getNextTitleId() == null) return PointResultDTO.fail("승급 불가능한 등급입니다.");

        // 🌟 [동적 난이도 계산] 티어(tierOrder)에 따른 수치 설정
        int tier = ut.getTierOrder(); 
        int targetLevel; // 승급을 위해 필요한 강화 수치
        int pityLimit;   // 확정 승급을 위한 최대 실패 횟수

        if (tier <= 2) { targetLevel = 3; pityLimit = 15; }
        else if (tier <= 5) { targetLevel = 4; pityLimit = 20; }
        else if (tier <= 8) { targetLevel = 5; pityLimit = 30; }
        else { targetLevel = 7; pityLimit = 50; }

        pointMapper.deductPoint(userId, ENHANCE_COST);
        int remainPoint = currentPoint - ENHANCE_COST;

        // 1. 천장 판정
        if (ut.getFailCount() >= (pityLimit - 1)) {
            pointMapper.deleteUserTitle(userId, titleId);
            pointMapper.insertUserTitle(userId, ut.getNextTitleId());
            return PointResultDTO.ok("✨ [천장 도달] " + pityLimit + "회 실패 보정으로 승급했습니다!", remainPoint);
        }

        // 2. 강화 성공 판정 (30% 확률)
        if (random.nextDouble() < 0.30) {
            // 목표 레벨 달성 시 승급
            if (ut.getEnhanceLevel() >= (targetLevel - 1)) {
                pointMapper.deleteUserTitle(userId, titleId);
                pointMapper.insertUserTitle(userId, ut.getNextTitleId());
                return PointResultDTO.ok("🎉 대성공! +" + targetLevel + " 달성으로 다음 티어로 승급!", remainPoint);
            } else {
                pointMapper.updateEnhanceSuccess(userId, titleId);
                return PointResultDTO.ok("✨ 강화 성공! (+" + (ut.getEnhanceLevel() + 1) + ")", remainPoint);
            }
        } else {
            // 3. 실패 판정 (50% 확률로 레벨 하락)
            boolean dropLevel = (ut.getEnhanceLevel() > 0) && (random.nextDouble() < 0.50);
            pointMapper.updateEnhanceFail(userId, titleId, dropLevel);
            String msg = dropLevel ? "💥 실패! 레벨 하락." : "💦 실패... 레벨 유지.";
            return PointResultDTO.fail(msg + " (누적 실패: " + (ut.getFailCount() + 1) + "/" + pityLimit + ")");
        }
    }
}