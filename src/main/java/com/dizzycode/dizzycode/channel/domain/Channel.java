package com.dizzycode.dizzycode.channel.domain;

import com.dizzycode.dizzycode.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Channel {

    private Long channelId;
    private Category category;
    private String channelName;
    private ChannelType channelType;

    @Builder
    public Channel(Long channelId, Category category, String channelName, ChannelType channelType) {
        this.channelId = channelId;
        this.category = category;
        this.channelName = channelName;
        this.channelType = channelType;
    }
}
