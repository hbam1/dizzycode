package com.dizzycode.dizzycode.room.infrastructure;

import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.service.port.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository roomJpaRepository;

    @Override
    public Optional<Room> findByRoomId(Long roomId) {
        return roomJpaRepository.findByRoomId(roomId).map(RoomEntity::toModel);
    }

    @Override
    public List<Room> findRoomsByOpenIs(boolean open) {
        return roomJpaRepository.findRoomsByOpenIs(open).stream()
                .map(roomEntity -> {
                    Room room = roomEntity.toModel();

                    return room;
                })
                .toList();
    }

    @Override
    public Room save(Room room) {
        return roomJpaRepository.save(RoomEntity.fromModel(room)).toModel();
    }

    @Override
    public void delete(Room room) {
        roomJpaRepository.delete(RoomEntity.fromModel(room));
    }
}
