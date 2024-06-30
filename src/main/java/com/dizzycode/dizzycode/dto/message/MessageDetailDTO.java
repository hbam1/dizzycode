package com.dizzycode.dizzycode.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDetailDTO {

    private String senderUsername;
    private String content;
    private LocalDateTime timestamp; // 메시지 생성 시간 추가
}
