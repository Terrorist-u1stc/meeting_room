package com.group4.meetingroom.entity;

import java.time.LocalDateTime;

public class RoomReservation {
    private Integer rReservationId;
    private Integer userId;
    private Integer roomId;
    private String roomName;
    private String userName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int attendees = 0;
    public Integer getrReservationId() {
        return rReservationId;
    }

    public void setrReservationId(Integer rReservationId) {
        this.rReservationId = rReservationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getAttendees() {
        return attendees;
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
    }
}
