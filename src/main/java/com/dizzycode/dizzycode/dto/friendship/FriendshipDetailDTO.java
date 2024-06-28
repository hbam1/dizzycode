package com.dizzycode.dizzycode.dto.friendship;

import com.dizzycode.dizzycode.domain.friendship.Friendship;
import com.dizzycode.dizzycode.domain.friendship.FriendshipId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendshipDetailDTO {

    private Long friendId;
    private String friendName;
    private Friendship.FriendshipStatus currentStatus;
}
