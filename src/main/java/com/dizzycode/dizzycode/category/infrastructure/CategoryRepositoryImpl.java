package com.dizzycode.dizzycode.category.infrastructure;

import com.dizzycode.dizzycode.category.domain.dto.CategoryDetailDTO;
import com.dizzycode.dizzycode.category.service.port.CategoryRepository;
import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.channel.domain.dto.ChannelDetailDTO;
import com.dizzycode.dizzycode.channel.infrastructure.ChannelJpaRepository;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.infrastructure.RoomEntity;
import com.dizzycode.dizzycode.room.infrastructure.RoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final RoomJpaRepository roomJpaRepository;
    private final ChannelJpaRepository channelJpaRepository;

    @Override
    public List<CategoryDetailDTO> findCategoriesByRoom(Long roomId) throws ClassNotFoundException {
        RoomEntity room = roomJpaRepository.findByRoomId(roomId).orElseThrow(() -> new ClassNotFoundException("방이 존재하지 않습니다."));

        return categoryJpaRepository.findCategoriesByRoom(room).stream()
                .map(category -> {
                    CategoryDetailDTO categoryDetailDTO = new CategoryDetailDTO();
                    categoryDetailDTO.setCategoryName(category.getCategoryName());
                    categoryDetailDTO.setRoomId(category.getRoom().getRoomId());
                    categoryDetailDTO.setCategoryId(category.getCategoryId());

                    List<ChannelDetailDTO> channelDetailDTOs = channelJpaRepository.findChannelsByCategory(category).stream()
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
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findCategoryByCategoryId(Long categoryId) {
        return categoryJpaRepository.findCategoryByCategoryId(categoryId).map(CategoryEntity::toModel);
    }

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(CategoryEntity.fromModel(category)).toModel();
    }
}
