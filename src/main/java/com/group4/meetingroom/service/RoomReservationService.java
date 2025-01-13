package com.group4.meetingroom.service;

import com.group4.meetingroom.entity.Booking;
import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.RoomReservation;
import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.mapper.RoomMapper;
import com.group4.meetingroom.mapper.RoomReservationMapper;
import com.group4.meetingroom.mapper.UserMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomReservationService {
    @Autowired
    RoomReservationMapper roomReservationMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    UserMapper userMapper;
    public MessageModel<RoomReservation> selectById(Integer id){
    MessageModel<RoomReservation> result = new MessageModel<>();
    return result;
}
    public Map<String, Object> reserveRoom(Booking booking) {

        RoomReservation roomReservation = new RoomReservation();
        roomReservation.setUserName(booking.getUserName());
        roomReservation.setUserId(booking.getUserId());
        roomReservation.setEndTime(booking.getEndTime());
        roomReservation.setStartTime(booking.getStartTime());
        roomReservation.setRoomName(booking.getRoomName());

        Map<String, Object> response = new HashMap<>();
        //本来是直接通过ID来查询，由于前端发来的是会议室名，先通过会议室名查id，再通过id来查询
        String roomName = roomReservation.getRoomName();
        MeetingRoom room;
        Integer roomId;
        System.out.println(roomReservation.getUserName());
        if(roomName != null && !roomName.trim().isEmpty())
        { room = roomMapper.selectByName(roomName);
         roomId = room.getId();
         roomReservation.setRoomId(roomId);}
        else {
             roomId = roomReservation.getRoomId();
             room = roomMapper.selectById(roomId);
        }
        User user = userMapper.selectById(roomReservation.getUserId());

        if (room == null) {
            response.put("success", false);
            response.put("message", "该会议室不存在");
            response.put("meetingRooms", null);
            return response;
        }

        // 检查会议室状态是否可用
        if (room.getStatus() != 1) {
            response.put("success", false);
            response.put("message", "该会议室目前不可用");
            response.put("meetingRooms", null);
            return response;
        }

        // 检查预约时间是否冲突
        List<RoomReservation> reservations = roomReservationMapper.findReservationTimesByRoomId(roomId);
        for (RoomReservation existingReservation : reservations) {
            if (roomReservation.getStartTime().isBefore(existingReservation.getEndTime()) &&
                    roomReservation.getEndTime().isAfter(existingReservation.getStartTime())) {
                response.put("success", false);
                response.put("message", "预约时间存在冲突");
                response.put("meetingRooms", null);
                return response;
            }
        }
        try{
            // 插入预约记录
            roomReservationMapper.insertReservation(roomReservation);
            response.put("success", true);
            response.put("message", "已成功预约");
            Map<String, Object> meetingRoom = new HashMap<>();
            meetingRoom.put("roomName", room.getRoomName());
            meetingRoom.put("location", room.getLocation());
            meetingRoom.put("capacity", room.getCapacity());
            meetingRoom.put("availableTime", "9:00 - 18:00");
            meetingRoom.put("reservedBy", user.getUserName());
            meetingRoom.put("participants" ,4);
            response.put("meetingrooms",meetingRoom);
            System.out.println("Response: " + response);
            return response;
        }catch (PersistenceException e){
            response.put("success", false);
            response.put("message", "预约失败");
            response.put("meetingRooms", null);
            return response;
        }
    }
    //查询预约记录
    public MessageModel<List<RoomReservation>> getBookings(String date, Integer userId){
        List<RoomReservation> reservations;
        if(date == null){
            reservations = roomReservationMapper.findByUserId(userId);
        }else
        {reservations = roomReservationMapper.findByUserIdAndDate(userId, date);}
        if (reservations != null && !reservations.isEmpty()) {
            for(RoomReservation reservation: reservations){
                //统计参会人数
                reservation.setParticipants(roomReservationMapper.countAttendees(reservation.getrReservationId()));
            }
            // 如果有预定，返回成功状态
            return new MessageModel<>(1, "成功", reservations);
        } else {
            // 如果没有预定，返回失败状态
            return new MessageModel<>(0, "没有找到预定记录", null);
        }
    }
}
