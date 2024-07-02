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
@Document(collection = "direct_messages")
public class DirectMessage {

    @Id
    private final String messageId;

    @Field("member_id")
    private Long memberId;

    @Field("member_username")
    private String memberUsername;

    @Field("friendship_id")
    private String friendshipId;

    @Field("content")
    private String content;

    @Field("image_url")
    private String imageUrl;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public DirectMessage() {
        this.messageId = UUID.randomUUID().toString();
    }
}
