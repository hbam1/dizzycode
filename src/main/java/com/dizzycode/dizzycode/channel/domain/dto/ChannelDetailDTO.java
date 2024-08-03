package com.dizzycode.dizzycode.channel.domain.dto;

import com.dizzycode.dizzycode.channel.domain.ChannelType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDetailDTO {

    private Long channelId;
    private Long categoryId;
    private String channelName;
    private ChannelType channelType;
}
