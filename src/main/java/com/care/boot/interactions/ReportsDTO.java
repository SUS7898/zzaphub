package com.care.boot.interactions;

public class ReportsDTO {
    private int id;
    private int reporterId;
    private String targetType; // 'POST' 또는 'COMMENT'
    private int targetId;
    private String reason;
    private String status;     // '대기중' 등
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReporterId() {
		return reporterId;
	}
	public void setReporterId(int reporterId) {
		this.reporterId = reporterId;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public int getTargetId() {
		return targetId;
	}
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
}
