package com.group4.meetingroom.service;

import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.mapper.RoomMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomMapper roomMapper;
    //新增会议室
    public MessageModel<MeetingRoom> addRoom(MeetingRoom room){
        MessageModel<MeetingRoom> m = new MessageModel<>();
        try{
            roomMapper.addRoom(room);
            m.setStatus(HttpStatus.OK.value());
            m.setMsg("会议室添加成功");
            m.setData(room);
            return m;
        }catch (PersistenceException e){
            m.setStatus(400);
            m.setMsg("添加失败");
            m.setData(room);
            return m;
        }
    }
    //查询会议室
    public MessageModel<MeetingRoom> selectRoom(int id){
        MessageModel<MeetingRoom> m = new MessageModel<>();
        try{
            MeetingRoom room = roomMapper.selectById(id);
            m.setStatus(HttpStatus.OK.value());
            m.setMsg("查询成功");
            m.setData(room);
            return m;
        }catch (PersistenceException e){
            m.setStatus(0);
            m.setMsg("该会议室不存在");
            m.setData(null);
            return m;
        }
    }
}
