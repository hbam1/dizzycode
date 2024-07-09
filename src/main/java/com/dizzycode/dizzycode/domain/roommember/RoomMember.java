package com.dizzycode.dizzycode.domain.roommember;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "room_members")
public class RoomMember {

    @EmbeddedId
    private RoomMemberId roomMemberId;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id", updatable = false)
    private Room room;

    @Column(nullable = false)
    private boolean manager = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
