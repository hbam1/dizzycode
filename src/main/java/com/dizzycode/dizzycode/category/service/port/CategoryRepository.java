package com.dizzycode.dizzycode.category.service.port;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.category.domain.dto.CategoryDetailDTO;
import com.dizzycode.dizzycode.room.exception.NoRoomException;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<CategoryDetailDTO> findCategoriesByRoom(Long roomId) throws NoRoomException;
    Optional<Category> findCategoryByCategoryId(Long categoryId);
    Category save(Long roomId, String categoryName);
}
