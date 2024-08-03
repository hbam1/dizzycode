package com.dizzycode.dizzycode.message.infrastructure;

import com.dizzycode.dizzycode.message.domain.DirectMessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRoomRepository extends JpaRepository<DirectMessageRoom, Long> {

    DirectMessageRoom findByRoomId(Long roomId);

    @Query("SELECT dmr FROM DirectMessageRoom dmr WHERE dmr.friendshipId = :friendshipId")
    DirectMessageRoom findByFriendshipId(String friendshipId);
}
