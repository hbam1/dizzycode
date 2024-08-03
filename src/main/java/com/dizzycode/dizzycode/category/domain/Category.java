package com.dizzycode.dizzycode.category.domain;

import com.dizzycode.dizzycode.channel.domain.Channel;
import com.dizzycode.dizzycode.room.domain.Room;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Category {

    private Long categoryId;
    private Room room;
    private String categoryName;
    private List<Channel> channels;

    @Builder
    public Category(Long categoryId, Room room, String categoryName, List<Channel> channels) {
        this.categoryId = categoryId;
        this.room = room;
        this.categoryName = categoryName;
        this.channels = channels;
    }
}
