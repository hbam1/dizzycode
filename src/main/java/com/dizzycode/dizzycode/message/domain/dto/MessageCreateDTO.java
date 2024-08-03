package com.dizzycode.dizzycode.message.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageCreateDTO {

    private Long senderId;
    private String content;
    private String url;
}
