package com.group4.meetingroom.controller;

import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class RoomController {

    @Autowired
    RoomService roomService;
    //添加会议室或更新会议室，前端传递的是roomid，如果更新信息时，能把id也传来就好了。
    @CrossOrigin(origins = "*")
    @PostMapping("/meetingRooms")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageModel<MeetingRoom> addRoom(@RequestBody MeetingRoom room, @RequestParam String action) {
        MessageModel<MeetingRoom> m = new MessageModel<>();
        if (action.equals("add")){
            m = roomService.addRoom(room);
        }else if (action.equals("update")){
            m = roomService.updateRoom(room);
        }
        return m;
    }
    //查询会议室
    @GetMapping("/room/{id}")
    public MessageModel<MeetingRoom> getRoomById(@PathVariable Integer id) {return roomService.selectRoom(id);}
    @GetMapping("/getMeetingRooms")
    public MessageModel<List<MeetingRoom>> getAll(){
        return roomService.getAll();
    }
    //推荐会议室
}
