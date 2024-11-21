package com.group4.meetingroom.controller;

import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController

public class RoomController {
    @Autowired
    RoomService roomService;
    //添加会议室
    @PostMapping("/addRoom")
    public MessageModel<MeetingRoom> addRoom(@RequestBody MeetingRoom room) {
        return roomService.addRoom(room);
    }
    //查询会议室
    @GetMapping("/room/{id}")
    public MessageModel<MeetingRoom> getRoomById(@PathVariable Integer id) {
        return roomService.selectRoom(id);
       }
}
