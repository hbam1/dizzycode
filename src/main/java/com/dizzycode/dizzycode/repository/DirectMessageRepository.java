package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.DirectMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRepository extends MongoRepository<DirectMessage, String> {
}
