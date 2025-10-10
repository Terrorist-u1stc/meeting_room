package com.group4.meetingroom.entity;

public class RoomStatistics {
    private int roomId;          // 会议室ID
    private int usageCount;      // 使用次数
    private long totalDuration;  // 总使用时长（分钟）
    private int totalAttendees;  // 总参会人数
    private double avgAttendees; // 平均参会人数
    private double avgDuration;

    public RoomStatistics(int roomId, int usageCount, long totalDuration, int totalAttendees, double avgAttendees) {
        this.roomId = roomId;
        this.usageCount = usageCount;
        this.totalDuration = totalDuration;
        this.totalAttendees = totalAttendees;
        this.avgAttendees = avgAttendees;
        avgDuration = (double)totalDuration / usageCount;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getTotalAttendees() {
        return totalAttendees;
    }

    public void setTotalAttendees(int totalAttendees) {
        this.totalAttendees = totalAttendees;
    }

    public double getAvgAttendees() {
        return avgAttendees;
    }

    public void setAvgAttendees(double avgAttendees) {
        this.avgAttendees = avgAttendees;
    }

    public double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(double avgDuration) {
        this.avgDuration = avgDuration;
    }
}
