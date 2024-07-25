package com.dizzycode.dizzycode.domain;

import com.dizzycode.dizzycode.domain.roommember.DMRoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "direct_message_rooms")
public class DirectMessageRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column
    private String roomName;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DMRoomMember> roomMembers;

    @Column
    private String friendshipId;

    @Column
    private boolean groupChat = false;

    @Column
    private boolean closed = false;
}
