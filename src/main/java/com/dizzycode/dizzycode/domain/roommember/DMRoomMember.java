package com.dizzycode.dizzycode.domain.roommember;

import com.dizzycode.dizzycode.domain.DirectMessageRoom;
import com.dizzycode.dizzycode.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "direct_message_room_members")
public class DMRoomMember {

    @EmbeddedId
    private RoomMemberId roomMemberId;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id", updatable = false)
    private DirectMessageRoom room;
}
