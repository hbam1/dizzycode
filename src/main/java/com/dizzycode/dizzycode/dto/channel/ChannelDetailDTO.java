package com.dizzycode.dizzycode.dto.channel;

import com.dizzycode.dizzycode.domain.Channel;
import lombok.Getter;
import lombok.Setter;

import static com.dizzycode.dizzycode.domain.Channel.*;

@Getter
@Setter
public class ChannelDetailDTO {

    private Long channelId;
    private Long categoryId;
    private String channelName;
    private ChannelType channelType;
}
