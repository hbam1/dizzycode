package com.dizzycode.dizzycode.channel.domain.dto;

import com.dizzycode.dizzycode.channel.domain.ChannelType;
import lombok.Getter;
import lombok.Setter;

import static com.dizzycode.dizzycode.channel.infrastructure.ChannelEntity.*;

@Getter
@Setter
public class ChannelCreateDTO {

    private String channelName;
    private ChannelType channelType;
}
