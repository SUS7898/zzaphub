package com.care.boot.point;

public class UserTitleDTO {
    private Long userId;
    private Integer titleId;
    private boolean isEquipped; // boolean 타입
    private int enhanceLevel;
    private int failCount;
    
    private int tierOrder; // DB의 titles 테이블 tier_order 값 저장용
    
    
    private String titleType; // 'PROGRESSION' 또는 'GACHA' 저장

    public String getTitleType() { return titleType; }
    public void setTitleType(String titleType) { this.titleType = titleType; }
    
    // 조인 데이터
    private Integer nextTitleId; 
    private Integer price;
    private String titleName; 

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getTitleId() { return titleId; }
    public void setTitleId(Integer titleId) { this.titleId = titleId; }
    
    // 🌟 이 부분이 수정되었습니다. (isEquipped -> getIsEquipped)
    public boolean getIsEquipped() { return isEquipped; }
    public void setIsEquipped(boolean isEquipped) { this.isEquipped = isEquipped; }
    public int getTierOrder() { return tierOrder; }
    public void setTierOrder(int tierOrder) { this.tierOrder = tierOrder; }
    public int getEnhanceLevel() { return enhanceLevel; }
    public void setEnhanceLevel(int enhanceLevel) { this.enhanceLevel = enhanceLevel; }
    public int getFailCount() { return failCount; }
    public void setFailCount(int failCount) { this.failCount = failCount; }
    public Integer getNextTitleId() { return nextTitleId; }
    public void setNextTitleId(Integer nextTitleId) { this.nextTitleId = nextTitleId; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getTitleName() { return titleName; }
    public void setTitleName(String titleName) { this.titleName = titleName; }
}