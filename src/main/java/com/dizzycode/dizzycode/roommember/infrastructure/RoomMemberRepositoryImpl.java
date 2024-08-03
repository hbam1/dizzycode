package com.dizzycode.dizzycode.roommember.infrastructure;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import com.dizzycode.dizzycode.roommember.service.port.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomMemberRepositoryImpl implements RoomMemberRepository {

    private final RoomMemberJpaRepository roomMemberJpaRepository;

    @Override
    public List<Room> findRoomsByMemberId(Long memberID) {
        return roomMemberJpaRepository.findRoomsByMemberId(memberID).stream()
                .map(roomEntity -> {
                    Room room = roomEntity.toModel();
                    return room;
                })
                .toList();
    }

    @Override
    public List<Member> findMembersByRoomId(Long roomId) {
        return roomMemberJpaRepository.findMembersByRoomId(roomId).stream()
                .map(memberEntity -> {
                    Member member = memberEntity.toModel();

                    return member;
                })
                .toList();
    }

    @Override
    public Optional<RoomMember> findRoomMemberByRoomMemberId(RoomMemberId roomMemberId) {
        return roomMemberJpaRepository.findRoomMemberByRoomMemberId(RoomMemberIdEntity.fromModel(roomMemberId)).map(RoomMemberEntity::toModel);
    }

    @Override
    public RoomMember save(RoomMember roomMember) {
        return roomMemberJpaRepository.save(RoomMemberEntity.fromModel(roomMember)).toModel();
    }

    @Override
    public void delete(RoomMember roomMember) {
        roomMemberJpaRepository.delete(RoomMemberEntity.fromModel(roomMember));
    }
}
