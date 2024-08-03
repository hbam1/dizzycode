package com.dizzycode.dizzycode.category.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPostResponseDTO {

    private Long categoryId;
    private Long roomId;
    private String categoryName;
}
