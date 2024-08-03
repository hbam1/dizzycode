package com.dizzycode.dizzycode.room.domain;

import com.dizzycode.dizzycode.category.infrastructure.CategoryEntity;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Room {

    private Long roomId;
    private String roomName;
    private List<RoomMemberEntity> roomMemberEntities;
    private List<CategoryEntity> categories;
    private boolean open;

    @Builder
    public Room(Long roomId, String roomName, List<RoomMemberEntity> roomMemberEntities, List<CategoryEntity> categories, boolean open) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomMemberEntities = roomMemberEntities;
        this.categories = categories;
        this.open = open;
    }
}
