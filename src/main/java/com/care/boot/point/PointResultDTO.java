package com.care.boot.point;

public class PointResultDTO {
    private boolean success;
    private String message;
    private int remainPoint;

    public static PointResultDTO ok(String msg, int point) {
        PointResultDTO dto = new PointResultDTO();
        dto.setSuccess(true);
        dto.setMessage(msg);
        dto.setRemainPoint(point);
        return dto;
    }

    public static PointResultDTO fail(String msg) {
        PointResultDTO dto = new PointResultDTO();
        dto.setSuccess(false);
        dto.setMessage(msg);
        return dto;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getRemainPoint() { return remainPoint; }
    public void setRemainPoint(int remainPoint) { this.remainPoint = remainPoint; }
}