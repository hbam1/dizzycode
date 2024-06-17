package com.dizzycode.dizzycode.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "messages")
public class Message {

    @Id
    private final String messageId;

    @Field("member_id")
    private String memberId;

    @Field("room_id")
    private String roomId;

    @Field("channel_id")
    private String channelId;

    @Field("content")
    private String content;

    @Field("image_url")
    private String imageUrl;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Message() {
        this.messageId = UUID.randomUUID().toString(); // Custom UUID as _id
    }
}
