package com.dizzycode.dizzycode.channel.infrastructure;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.category.infrastructure.CategoryEntity;
import com.dizzycode.dizzycode.channel.domain.Channel;
import com.dizzycode.dizzycode.channel.service.port.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChannelRepositoryImpl implements ChannelRepository {

    private final ChannelJpaRepository channelJpaRepository;

    @Override
    public List<Channel> findChannelsByCategory(Category category) {
        return channelJpaRepository.findChannelsByCategory(CategoryEntity.fromModel(category)).stream()
                .map(channelEntity -> {
                    Channel channel = channelEntity.toModel();

                    return channel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Channel> findChannelByChannelId(Long channelId) {
        return channelJpaRepository.findChannelByChannelId(channelId).map(ChannelEntity::toModel);
    }

    @Override
    public Channel save(Channel channel) {
        return channelJpaRepository.save(ChannelEntity.fromModel(channel)).toModel();
    }
}
