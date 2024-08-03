package com.dizzycode.dizzycode.room.domain.room;

import com.dizzycode.dizzycode.channel.domain.ChannelType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.dizzycode.dizzycode.channel.infrastructure.ChannelEntity.*;

@Getter
@Setter
public class RoomCreateWithCCDTO {

    private Long roomId;
    private String roomName;
    private boolean open;
    private List<Category> categories;

    @Getter
    @Setter
    public static class Category {

        private Long categoryId;
        private String categoryName;
        private List<Channel> channels;
    }

    @Getter
    @Setter
    public static class Channel {

        private Long channelId;
        private String channelName;
        private ChannelType channelType;
    }
}
