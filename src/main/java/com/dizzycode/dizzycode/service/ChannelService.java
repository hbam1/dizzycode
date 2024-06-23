package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Category;
import com.dizzycode.dizzycode.domain.Channel;
import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.dto.channel.ChannelCreateDTO;
import com.dizzycode.dizzycode.dto.channel.ChannelDetailDTO;
import com.dizzycode.dizzycode.repository.CategoryRepository;
import com.dizzycode.dizzycode.repository.ChannelRepository;
import com.dizzycode.dizzycode.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;

    public ChannelDetailDTO createChannel(Long categoryId, ChannelCreateDTO channelCreateDTO) {

        Category category = categoryRepository.findCategoryByCategoryId(categoryId);

        Channel channel = new Channel();
        channel.setCategory(category);
        channel.setChannelName(channelCreateDTO.getChannelName());

        channel = channelRepository.save(channel);

        ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
        channelDetailDTO.setChannelId(channel.getChannelId());
        channelDetailDTO.setChannelName(channel.getChannelName());
        channelDetailDTO.setCategoryId(channel.getCategory().getCategoryId());

        return channelDetailDTO;
    }

    public List<ChannelDetailDTO> channelList(Long categoryId) {

        Category category = categoryRepository.findCategoryByCategoryId(categoryId);

        List<ChannelDetailDTO> channelDTOs = channelRepository.findChannelsByCategory(category).stream()
                .map(channel -> {
                    ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
                    channelDetailDTO.setChannelId(channel.getChannelId());
                    channelDetailDTO.setChannelName(channel.getChannelName());
                    channelDetailDTO.setCategoryId(categoryId);
                    return channelDetailDTO;
                })
                .collect(Collectors.toList());

        return channelDTOs;
    }

    public ChannelDetailDTO channelRetrieve(Long categoryId, Long channelId) {
        Channel channel = channelRepository.findChannelByChannelId(channelId);
        ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
        channelDetailDTO.setCategoryId(categoryId);
        channelDetailDTO.setChannelId(channel.getChannelId());
        channelDetailDTO.setChannelName(channel.getChannelName());

        return channelDetailDTO;
    }
}
