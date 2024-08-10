package com.dizzycode.dizzycode.category.service;

import com.dizzycode.dizzycode.category.domain.dto.CategoryCreateDTO;
import com.dizzycode.dizzycode.category.domain.dto.CategoryDetailDTO;
import com.dizzycode.dizzycode.category.domain.dto.CategoryPostResponseDTO;
import com.dizzycode.dizzycode.category.service.port.CategoryRepository;
import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.channel.service.port.ChannelRepository;
import com.dizzycode.dizzycode.channel.domain.dto.ChannelDetailDTO;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.exception.NoRoomException;
import com.dizzycode.dizzycode.room.service.port.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ChannelRepository channelRepository;

    @Transactional
    public CategoryPostResponseDTO createCategory(Long roomId, CategoryCreateDTO categoryCreateDTO) throws NoRoomException {
        Category category = categoryRepository.save(roomId, categoryCreateDTO.getCategoryName());
        CategoryPostResponseDTO categoryPostResponseDTO = new CategoryPostResponseDTO();
        categoryPostResponseDTO.setCategoryId(category.getCategoryId());
        categoryPostResponseDTO.setRoomId(category.getRoom().getRoomId());
        categoryPostResponseDTO.setCategoryName(category.getCategoryName());

        return categoryPostResponseDTO;
    }

    @Transactional
    public List<CategoryDetailDTO> categoryList(Long roomId) throws NoRoomException {
        return categoryRepository.findCategoriesByRoom(roomId);
    }

    @Transactional
    public CategoryDetailDTO categoryRetrieve(Long roomId, Long categoryId) {

        //TODO 예외처리
        Category category = categoryRepository.findCategoryByCategoryId(categoryId).orElseThrow();
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
