package com.dizzycode.dizzycode.category.infrastructure;

import com.dizzycode.dizzycode.room.infrastructure.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findCategoriesByRoom(RoomEntity roomEntity);
    Optional<CategoryEntity> findCategoryByCategoryId(Long categoryId);
}
