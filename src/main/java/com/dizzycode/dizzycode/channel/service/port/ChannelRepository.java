package com.dizzycode.dizzycode.channel.service.port;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.channel.domain.Channel;
import com.dizzycode.dizzycode.channel.domain.dto.ChannelCreateDTO;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {

    List<Channel> findChannelsByCategory(Category category);
    Optional<Channel> findChannelByChannelId(Long channelId);
    Channel save(Long categoryId, ChannelCreateDTO channelCreateDTO);
}
