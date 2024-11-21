package com.group4.meetingroom.controller;

import com.group4.meetingroom.entity.RoomReservation;
import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.service.RoomReservationService;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RservationController {
    @Autowired
    private RoomReservationService reservationService;
    @PostMapping("/reserve")
    public MessageModel<RoomReservation> reserveRoom(
            @RequestBody RoomReservation roomReservation,
            HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null){
            MessageModel<RoomReservation> response = new MessageModel<>();
            response.setStatus(401);
            response.setMsg("用户未登录，请先登录！");
            return response;
        }
        return reservationService.reserveRoom(roomReservation);
    }
}
