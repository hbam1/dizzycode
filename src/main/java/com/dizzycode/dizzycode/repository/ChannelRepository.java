package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Long, Channel> {
}
