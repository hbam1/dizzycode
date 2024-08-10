package com.dizzycode.dizzycode.category.infrastructure;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.channel.domain.Channel;
import com.dizzycode.dizzycode.channel.infrastructure.ChannelEntity;
import com.dizzycode.dizzycode.room.infrastructure.RoomEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @Column(nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelEntity> channels;

    public static CategoryEntity fromModel(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setRoom(RoomEntity.fromModel(category.getRoom()));
        categoryEntity.setCategoryName(category.getCategoryName());

        return categoryEntity;
    }

    public Category toModel() {
        return Category.builder()
                .room(room.toModel())
                .categoryName(categoryName)
                .build();
    }
}
