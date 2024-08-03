package com.dizzycode.dizzycode.category.service.port;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.room.domain.Room;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> findCategoriesByRoom(Room room);
    Optional<Category> findCategoryByCategoryId(Long categoryId);
    Category save(Category category);
}
