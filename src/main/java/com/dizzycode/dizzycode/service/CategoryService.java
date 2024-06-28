package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.*;
import com.dizzycode.dizzycode.dto.category.CategoryCreateDTO;
import com.dizzycode.dizzycode.dto.category.CategoryDetailDTO;
import com.dizzycode.dizzycode.dto.category.CategoryPostResponseDTO;
import com.dizzycode.dizzycode.dto.channel.ChannelDetailDTO;
import com.dizzycode.dizzycode.repository.CategoryRepository;
import com.dizzycode.dizzycode.repository.ChannelRepository;
import com.dizzycode.dizzycode.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RoomRepository roomRepository;
    private final ChannelRepository channelRepository;

    public CategoryPostResponseDTO createCategory(Long roomId, CategoryCreateDTO categoryCreateDTO) {

        Room room = roomRepository.findByRoomId(roomId);

        Category category = new Category();
        category.setRoom(room);
        category.setCategoryName(categoryCreateDTO.getCategoryName());

        category = categoryRepository.save(category);

        CategoryPostResponseDTO categoryPostResponseDTO = new CategoryPostResponseDTO();
        categoryPostResponseDTO.setCategoryId(category.getCategoryId());
        categoryPostResponseDTO.setRoomId(category.getRoom().getRoomId());
        categoryPostResponseDTO.setCategoryName(category.getCategoryName());

        return categoryPostResponseDTO;
    }

    public List<CategoryDetailDTO> categoryList(Long roomId) {

        Room room = roomRepository.findByRoomId(roomId);

        List<CategoryDetailDTO> categoryDetailDTOs = categoryRepository.findCategoriesByRoom(room).stream()
                .map(category -> {
                    CategoryDetailDTO categoryDetailDTO = new CategoryDetailDTO();
                    categoryDetailDTO.setCategoryName(category.getCategoryName());
                    categoryDetailDTO.setRoomId(category.getRoom().getRoomId());
                    categoryDetailDTO.setCategoryId(category.getCategoryId());

                    List<ChannelDetailDTO> channelDetailDTOs = channelRepository.findChannelsByCategory(category).stream()
                                    .map(channel -> {

                                        ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
                                        channelDetailDTO.setCategoryId(category.getCategoryId());
                                        channelDetailDTO.setChannelId(channel.getChannelId());
                                        channelDetailDTO.setChannelName(channel.getChannelName());
                                        channelDetailDTO.setChannelType(channel.getChannelType());

                                        return channelDetailDTO;
                                    }).toList();

                    categoryDetailDTO.setChannels(channelDetailDTOs);
                    return categoryDetailDTO;
                }).toList();

        return categoryDetailDTOs;
    }

    public CategoryDetailDTO categoryRetrieve(Long roomId, Long categoryId) {

        Category category = categoryRepository.findCategoryByCategoryId(categoryId);
        List<ChannelDetailDTO> channelDetailDTOs = channelRepository.findChannelsByCategory(category).stream()
                .map(channel -> {

                    ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
                    channelDetailDTO.setCategoryId(category.getCategoryId());
                    channelDetailDTO.setChannelId(channel.getChannelId());
                    channelDetailDTO.setChannelName(channel.getChannelName());

                    return channelDetailDTO;
                }).toList();

        CategoryDetailDTO categoryDetailDTO = new CategoryDetailDTO();
        categoryDetailDTO.setCategoryId(category.getCategoryId());
        categoryDetailDTO.setCategoryName(category.getCategoryName());
        categoryDetailDTO.setRoomId(roomId);
        categoryDetailDTO.setChannels(channelDetailDTOs);

        return categoryDetailDTO;
    }
}
