package com.dizzycode.dizzycode.channel.service;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.category.service.port.CategoryRepository;
import com.dizzycode.dizzycode.channel.domain.Channel;
import com.dizzycode.dizzycode.channel.service.port.ChannelRepository;
import com.dizzycode.dizzycode.channel.domain.dto.ChannelCreateDTO;
import com.dizzycode.dizzycode.channel.domain.dto.ChannelDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ChannelDetailDTO createChannel(Long categoryId, ChannelCreateDTO channelCreateDTO) {
        //TODO 예외처리
        Category category = categoryRepository.findCategoryByCategoryId(categoryId).orElseThrow();

        Channel channel = Channel.builder()
                .category(category)
                .channelName(channelCreateDTO.getChannelName())
                .channelType(channelCreateDTO.getChannelType())
                .build();
        channel = channelRepository.save(channel);

        ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
        channelDetailDTO.setChannelId(channel.getChannelId());
        channelDetailDTO.setChannelName(channel.getChannelName());
        channelDetailDTO.setCategoryId(channel.getCategory().getCategoryId());
        channelDetailDTO.setChannelType(channel.getChannelType());

        return channelDetailDTO;
    }

    @Transactional
    public List<ChannelDetailDTO> channelList(Long categoryId) {
        //TODO 예외처리
        Category category = categoryRepository.findCategoryByCategoryId(categoryId).orElseThrow();

        List<ChannelDetailDTO> channelDTOs = channelRepository.findChannelsByCategory(category).stream()
                .map(channel -> {
                    ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
                    channelDetailDTO.setChannelId(channel.getChannelId());
                    channelDetailDTO.setChannelName(channel.getChannelName());
                    channelDetailDTO.setCategoryId(categoryId);
                    channelDetailDTO.setChannelType(channel.getChannelType());

                    return channelDetailDTO;
                })
                .collect(Collectors.toList());

        return channelDTOs;
    }

    @Transactional
    public ChannelDetailDTO channelRetrieve(Long categoryId, Long channelId) {
        //TODO 예외처리
        Channel channel = channelRepository.findChannelByChannelId(channelId).orElseThrow();
        ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
        channelDetailDTO.setCategoryId(categoryId);
        channelDetailDTO.setChannelId(channel.getChannelId());
        channelDetailDTO.setChannelName(channel.getChannelName());
        channelDetailDTO.setChannelType(channel.getChannelType());

        return channelDetailDTO;
    }
}
