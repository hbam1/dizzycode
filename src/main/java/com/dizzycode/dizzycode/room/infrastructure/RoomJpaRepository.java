package com.dizzycode.dizzycode.room.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, Long> {
    Optional<RoomEntity> findByRoomId(Long roomId);
    List<RoomEntity> findRoomsByOpenIs(boolean open);
}
