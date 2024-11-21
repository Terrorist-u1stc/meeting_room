package com.group4.meetingroom.service;

import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.RoomReservation;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.mapper.RoomMapper;
import com.group4.meetingroom.mapper.RoomReservationMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomReservationService {
    @Autowired
    RoomReservationMapper roomReservationMapper;
    @Autowired
    RoomMapper roomMapper;
public MessageModel<RoomReservation> selectById(Integer id){
    MessageModel<RoomReservation> result = new MessageModel<>();
    return result;
}
    public MessageModel<RoomReservation> reserveRoom(RoomReservation roomReservation) {
        MessageModel<RoomReservation> response = new MessageModel<>();
        Integer roomId = roomReservation.getRoomId();
        MeetingRoom room = roomMapper.selectById(roomId);
        if (room == null) {
            response.setStatus(404);
            response.setMsg("会议室不存在！");
            return response;
        }

        // 检查会议室状态是否可用
        if (room.getStatus() != 1) {
            response.setStatus(400);
            response.setMsg("会议室不可用！");
            return response;
        }

        // 检查预约时间是否冲突
        List<RoomReservation> reservations = roomReservationMapper.findReservationTimesByRoomId(roomId);
        for (RoomReservation existingReservation : reservations) {
            if (roomReservation.getStartTime().isBefore(existingReservation.getEndTime()) &&
                    roomReservation.getEndTime().isAfter(existingReservation.getStartTime())) {
                response.setStatus(409);
                response.setMsg("预约时间冲突！");
                return response;
            }
        }
        try{
            // 插入预约记录
            roomReservationMapper.insertReservation(roomReservation);
            response.setStatus(HttpStatus.OK.value());
            response.setMsg("预约成功！");
            response.setData(roomReservation);
            return response;
        }catch (PersistenceException e){
            response.setStatus(409);
            response.setMsg("注册失败");
            response.setData(roomReservation);
            return response;
        }
    }
}
