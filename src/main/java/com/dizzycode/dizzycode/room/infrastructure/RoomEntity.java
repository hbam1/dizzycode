package com.dizzycode.dizzycode.room.infrastructure;

import com.dizzycode.dizzycode.category.infrastructure.CategoryEntity;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberEntity;
import com.dizzycode.dizzycode.room.domain.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false)
    private String roomName;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMemberEntity> roomMembers;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryEntity> categories;

    @Column(nullable = false)
    private boolean open;

    public static RoomEntity fromModel(Room room) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomName(room.getRoomName());
        roomEntity.setOpen(room.isOpen());

        return roomEntity;
    }

    public Room toModel() {
        return Room.builder()
                .roomId(roomId)
                .roomName(roomName)
                .open(open)
                .build();
    }
}
