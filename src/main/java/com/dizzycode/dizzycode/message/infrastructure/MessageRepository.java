package com.dizzycode.dizzycode.message.infrastructure;

import com.dizzycode.dizzycode.message.domain.RoomMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<RoomMessage, String> {

    @Query("{ 'channelId': :#{#channelId}, 'createdAt': { $lt: :#{#last}, $gt: :#{#first} } }")
    List<RoomMessage> findTop20ByChannelIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            @Param("channelId") Long channelId,
            @Param("last") LocalDateTime last,
            @Param("first") LocalDateTime first,
            Sort sort
    );

    @Query("{ 'channelId': :#{#channelId}, 'createdAt': { $gt: :#{#first} } }")
    List<RoomMessage> findTop20ByChannelIdAndCreatedAtAfterOrderByCreatedAtDesc(
            @Param("channelId") Long channelId,
            @Param("first") LocalDateTime first,
            Sort sort
    );

    default List<RoomMessage> findMessages(Long channelId, LocalDateTime last, LocalDateTime first) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (last != null) {
            return findTop20ByChannelIdAndCreatedAtBetweenOrderByCreatedAtDesc(channelId, last, first, sort);
        } else {
            return findTop20ByChannelIdAndCreatedAtAfterOrderByCreatedAtDesc(channelId, first, sort);
        }
    }
}
