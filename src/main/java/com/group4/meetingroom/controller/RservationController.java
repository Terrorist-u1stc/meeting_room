package com.group4.meetingroom.controller;

import com.group4.meetingroom.entity.RoomReservation;
import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.service.RoomReservationService;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RservationController {
    @Autowired
    private RoomReservationService reservationService;
    //预约会议室
    @CrossOrigin(origins = "*")
    @PostMapping("/api/meeting-rooms/availability")
    public Map<String, Object> reserveRoom(
            @RequestBody RoomReservation roomReservation,
            HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null){
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户未登录");
            response.put("meetingRooms", null);
            return response;}
        roomReservation.setUserId(user.getId());
        return reservationService.reserveRoom(roomReservation);
    }
    //查询预约记录
    @CrossOrigin(origins = "*")
    @PostMapping("/getMeetingRoomBookings")
    public MessageModel<List<RoomReservation>> userReserve(
            @RequestParam(required = false) String date,
            HttpSession session
    ){
        User user = (User) session.getAttribute("currentUser");
        if (user == null){
            MessageModel<List<RoomReservation>> response = new MessageModel<>();
            response.setMsg("用户未登录");
            response.setStatus(401);
            response.setData(null);
            return response;
        }
        return reservationService.getBookings(date, user.getId());
    }
}
