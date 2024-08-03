package com.dizzycode.dizzycode.category.infrastructure;

import com.dizzycode.dizzycode.category.service.port.CategoryRepository;
import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.infrastructure.RoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Category> findCategoriesByRoom(Room room) {
        return categoryJpaRepository.findCategoriesByRoom(RoomEntity.fromModel(room)).stream()
                .map(categoryEntity -> {
                    Category category = categoryEntity.toModel();

                    return category;
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
