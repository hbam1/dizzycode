package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.Category;
import com.dizzycode.dizzycode.domain.Channel;
import com.dizzycode.dizzycode.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findChannelsByCategory(Category category);
    Channel findChannelByChannelId(Long channelId);
}
