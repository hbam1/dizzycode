package com.dizzycode.dizzycode.dto.friendship;

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
