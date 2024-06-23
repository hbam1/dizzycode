package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    public Room findByRoomId(Long roomId);
}
