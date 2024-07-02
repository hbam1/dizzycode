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
    private  String messageId;

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

    @Field("image_url")
    private String imageUrl;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Message() {
        this.messageId = UUID.randomUUID().toString(); // Custom UUID as _id
    }
}
