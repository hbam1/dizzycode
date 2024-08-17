package com.dizzycode.dizzycode.message.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageCreateDTO {

    private Long senderId;
    private String content;
    private List<String> url;
}
