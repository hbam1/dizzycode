package com.dizzycode.dizzycode.category.domain.dto;

import com.dizzycode.dizzycode.channel.domain.dto.ChannelDetailDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryDetailDTO {

    private Long categoryId;
    private Long roomId;
    private String categoryName;
    private List<ChannelDetailDTO> channels;
}
