package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomId(Long roomId);

    List<Room> findRoomsByOpenIs(boolean open);
}
