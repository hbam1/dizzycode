package com.dizzycode.dizzycode.message.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDetailDTO {

    private String messageId;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp; // 메시지 생성 시간 추가
}
