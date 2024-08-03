package com.dizzycode.dizzycode.message.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "direct_messages")
public class DirectMessage {

    @Id
    private String id;

    @Field("member_id")
    private Long memberId;

    @Field("member_username")
    private String memberUsername;

    @Field("room_id")
    private Long roomId;

    @Field("content")
    private String content;

    @Field("image_url")
    private String imageUrl;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
