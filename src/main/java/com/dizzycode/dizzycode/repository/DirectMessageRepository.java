package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.DirectMessage;
import com.dizzycode.dizzycode.domain.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DirectMessageRepository extends MongoRepository<DirectMessage, String> {
    @Query("{ 'friendshipId': :#{#friendshipId}, 'createdAt': { $lt: :#{#last} } }")
    List<Message> findTop20ByFriendshipIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            @Param("friendshipId") String friendshipId,
            @Param("last") LocalDateTime last,
            Sort sort
    );

    default List<Message> findMessages(String friendshipId, LocalDateTime last) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (last == null) {
            return findTop20ByFriendshipId(friendshipId, sort);
        } else {
            return findTop20ByFriendshipIdAndCreatedAtBeforeOrderByCreatedAtDesc(friendshipId, last, sort);
        }
    }

    @Query("{ 'friendshipId': :#{#friendshipId} }")
    List<Message> findTop20ByFriendshipId(@Param("friendshipId") String friendshipId, Sort sort);
}
