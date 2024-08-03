package com.dizzycode.dizzycode.message.infrastructure;

import com.dizzycode.dizzycode.message.domain.DirectMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DirectMessageRepository extends MongoRepository<DirectMessage, Long> {

    @Query("{ 'roomId': :#{#roomId}, 'createdAt': { $lt: :#{#last} } }")
    List<DirectMessage> findTop20ByRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            @Param("roomId") Long roomId,
            @Param("last") LocalDateTime last,
            Sort sort
    );

    default List<DirectMessage> findMessages(Long roomId, LocalDateTime last) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (last == null) {
            return findTop20ByRoomId(roomId, sort);
        } else {
            return findTop20ByRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(roomId, last, sort);
        }
    }

    @Query("{ 'roomId': :#{#roomId} }")
    List<DirectMessage> findTop20ByRoomId(@Param("roomId") Long roomId, Sort sort);
}
