package com.dizzycode.dizzycode.message.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
