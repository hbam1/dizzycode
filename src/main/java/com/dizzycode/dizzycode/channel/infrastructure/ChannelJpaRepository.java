package com.dizzycode.dizzycode.channel.infrastructure;

import com.dizzycode.dizzycode.category.infrastructure.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelJpaRepository extends JpaRepository<ChannelEntity, Long> {
    List<ChannelEntity> findChannelsByCategory(CategoryEntity categoryEntity);
    Optional<ChannelEntity> findChannelByChannelId(Long channelId);
}
