package com.dizzycode.dizzycode.message.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "messages")
public class RoomMessage {

    @Id
    private String id; // MongoDB의 기본 ID 필드

    @Field("member_id")
    private Long memberId;

    @Field("member_username")
    private String memberUsername;

    @Field("room_id")
    private Long roomId;

    @Field("category_id")
    private Long categoryId;

    @Field("channel_id")
    private Long channelId;

    @Field("content")
    private String content;

    @Field("url")
    private List<String> url;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
