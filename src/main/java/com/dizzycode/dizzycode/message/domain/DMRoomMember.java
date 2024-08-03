package com.dizzycode.dizzycode.message.domain;

import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberIdEntity;
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
    private RoomMemberIdEntity roomMemberId;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id", updatable = false)
    private MemberEntity member;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id", updatable = false)
    private DirectMessageRoom room;
}
