package com.group4.meetingroom.service;

import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.RoomReservation;
import com.group4.meetingroom.entity.RoomStatistics;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.mapper.RoomMapper;
import com.group4.meetingroom.mapper.RoomReservationMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomReservationMapper r;
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
    public MessageModel<List<MeetingRoom>> getAll() {
        MessageModel<List<MeetingRoom>> message = new MessageModel<>();
        try {
            List<MeetingRoom> m = roomMapper.selectAll();
            for (MeetingRoom meetingRoom : m) {
                List<RoomReservation> roomReservations = r.selectAll(meetingRoom.getId());
                meetingRoom.setReservations(roomReservations);
            }
            message.setStatus(1);
            message.setMsg("查询成功");
            message.setData(m);
        }catch (Exception e){
            message.setStatus(0);
            message.setMsg("查询失败：" + e.getMessage());
        }
        return message;
    }
    //更新会议室
    public MessageModel<MeetingRoom> updateRoom(MeetingRoom room){
        MessageModel<MeetingRoom> m = new MessageModel<>();
        try{
            roomMapper.updateRoom(room);
            m.setStatus(HttpStatus.OK.value());
            m.setMsg("会议室更新成功");
            m.setData(room);
            return m;
        }catch (PersistenceException e){
            m.setStatus(400);
            m.setMsg("添加失败");
            m.setData(room);
            return m;
        }
    }

    public MessageModel<List<RoomStatistics>> getStats(String start, String end) {
        MessageModel<List<RoomStatistics>> message = new MessageModel<>();
        message.setData( roomMapper.getRoomStats(start, end));
        message.setStatus(HttpStatus.OK.value());
        message.setMsg("查询成功");
        return message;
    }
}
