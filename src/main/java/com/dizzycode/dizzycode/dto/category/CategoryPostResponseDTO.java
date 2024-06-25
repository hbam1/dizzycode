package com.dizzycode.dizzycode.dto.category;

import com.dizzycode.dizzycode.dto.channel.ChannelDetailDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryPostResponseDTO {

    private Long categoryId;
    private Long roomId;
    private String categoryName;
}
