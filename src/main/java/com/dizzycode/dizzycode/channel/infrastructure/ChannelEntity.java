package com.dizzycode.dizzycode.channel.infrastructure;

import com.dizzycode.dizzycode.category.infrastructure.CategoryEntity;
import com.dizzycode.dizzycode.channel.domain.Channel;
import com.dizzycode.dizzycode.channel.domain.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
@Table(name = "channels")
public class ChannelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false)
    private String channelName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType channelType;

    public static ChannelEntity fromModel(Channel channel) {
        ChannelEntity channelEntity = new ChannelEntity();
        channelEntity.setCategory(CategoryEntity.fromModel(channel.getCategory()));
        channelEntity.setChannelName(channel.getChannelName());
        channelEntity.setChannelType(channel.getChannelType());

        return channelEntity;
    }

    public Channel toModel() {
        return Channel.builder()
                .category(category.toModel())
                .channelName(channelName)
                .channelType(channelType)
                .build();
    }
}
