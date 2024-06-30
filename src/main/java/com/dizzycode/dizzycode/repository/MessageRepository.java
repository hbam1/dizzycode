package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    @Query("{ 'channelId': :#{#channelId}, 'createdAt': { $lt: :#{#last} } }")
    List<Message> findTop20ByChannelIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            @Param("channelId") Long channelId,
            @Param("last") LocalDateTime last,
            Sort sort
    );

    default List<Message> findMessages(Long channelId, LocalDateTime last) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (last == null) {
            return findTop20ByChannelId(channelId, sort);
        } else {
            return findTop20ByChannelIdAndCreatedAtBeforeOrderByCreatedAtDesc(channelId, last, sort);
        }
    }

    @Query("{ 'channelId': :#{#channelId} }")
    List<Message> findTop20ByChannelId(@Param("channelId") Long channelId, Sort sort);

}
