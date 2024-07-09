package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.DirectMessage;
import com.dizzycode.dizzycode.domain.DirectMessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRoomRepository extends JpaRepository<DirectMessageRoom, Long> {

    DirectMessageRoom findByRoomId(Long roomId);
}
