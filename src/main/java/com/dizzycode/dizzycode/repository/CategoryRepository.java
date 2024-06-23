package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.Category;
import com.dizzycode.dizzycode.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findCategoriesByRoom(Room room);
    public Category findCategoryByCategoryId(Long categoryId);
 }
