package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.dto.category.CategoryCreateDTO;
import com.dizzycode.dizzycode.dto.category.CategoryDetailDTO;
import com.dizzycode.dizzycode.dto.category.CategoryPostResponseDTO;
import com.dizzycode.dizzycode.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/rooms/{roomId}/categories")
    public ResponseEntity<CategoryPostResponseDTO> createCategory(@RequestBody CategoryCreateDTO categoryCreateDTO, @PathVariable Long roomId) {

        return new ResponseEntity<>(categoryService.createCategory(roomId, categoryCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/rooms/{roomId}/categories")
    public ResponseEntity<List<CategoryDetailDTO>> categoryList(@PathVariable Long roomId) {

        return new ResponseEntity<>(categoryService.categoryList(roomId), HttpStatus.OK);
    }

    @GetMapping("/rooms/{roomId}/channels/{categoryId}")
    public ResponseEntity<CategoryDetailDTO> categoryRetrieve(@PathVariable Long roomId, @PathVariable Long categoryId) {

        return new ResponseEntity<>(categoryService.categoryRetrieve(roomId, categoryId), HttpStatus.OK);
    }
}
