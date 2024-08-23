package com.dizzycode.dizzycode.room.infrastructure;

public interface RoomIndexer {
    void addRoomIndex(Long roomId, String roomName);
    void deleteRoomIndex(Long roomId);
}
