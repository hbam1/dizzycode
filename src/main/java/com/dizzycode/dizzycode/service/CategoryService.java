package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.*;
import com.dizzycode.dizzycode.dto.category.CategoryCreateDTO;
import com.dizzycode.dizzycode.dto.category.CategoryDetailDTO;
import com.dizzycode.dizzycode.repository.CategoryRepository;
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

    public CategoryDetailDTO createCategory(Long roomId, CategoryCreateDTO categoryCreateDTO) {

        Room room = roomRepository.findByRoomId(roomId);

        Category category = new Category();
        category.setRoom(room);
        category.setCategoryName(categoryCreateDTO.getCategoryName());

        category = categoryRepository.save(category);

        CategoryDetailDTO categoryDetailDTO = new CategoryDetailDTO();
        categoryDetailDTO.setCategoryId(category.getCategoryID());
        categoryDetailDTO.setRoomId(category.getRoom().getRoomId());
        categoryDetailDTO.setCategoryName(category.getCategoryName());

        return categoryDetailDTO;
    }

    public List<CategoryDetailDTO> categoryList(Long roomId) {

        Room room = roomRepository.findByRoomId(roomId);

        List<CategoryDetailDTO> categoryDetailDTOs = categoryRepository.findCategoriesByRoom(room).stream()
                .map(category -> {
                    CategoryDetailDTO categoryDetailDTO = new CategoryDetailDTO();
                    categoryDetailDTO.setCategoryName(category.getCategoryName());
                    categoryDetailDTO.setRoomId(category.getRoom().getRoomId());
                    categoryDetailDTO.setCategoryId(category.getCategoryID());
                    return categoryDetailDTO;
                }).toList();

        return categoryDetailDTOs;
    }

    public CategoryDetailDTO categoryRetrieve(Long roomId, Long categoryId) {

        Category category = categoryRepository.findCategoryByCategoryId(categoryId);
        CategoryDetailDTO categoryDetailDTO = new CategoryDetailDTO();
        categoryDetailDTO.setCategoryId(category.getCategoryID());
        categoryDetailDTO.setCategoryName(category.getCategoryName());
        categoryDetailDTO.setRoomId(roomId);

        return categoryDetailDTO;
    }
}
