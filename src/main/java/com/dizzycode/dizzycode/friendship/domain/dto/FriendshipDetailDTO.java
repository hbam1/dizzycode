package com.dizzycode.dizzycode.friendship.domain.dto;

import com.dizzycode.dizzycode.friendship.domain.FriendshipStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendshipDetailDTO {

    private Long friendId;
    private String friendName;
    private FriendshipStatus currentStatus;
}
