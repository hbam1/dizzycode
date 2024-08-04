package com.dizzycode.dizzycode.room.service.port;

import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.room.infrastructure.RoomEntity;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findByRoomId(Long roomId);
    List<Room> findRoomsByOpenIs(boolean open);
    RoomCreateWithCCDTO save(RoomCreateDTO roomCreateDTO);
    void delete(Long roomId) throws ClassNotFoundException;
}
