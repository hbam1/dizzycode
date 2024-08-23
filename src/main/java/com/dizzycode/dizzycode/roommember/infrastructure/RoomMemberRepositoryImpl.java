package com.dizzycode.dizzycode.roommember.infrastructure;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.exception.NoMemberException;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.infrastructure.RoomEntity;
import com.dizzycode.dizzycode.room.infrastructure.RoomJpaRepository;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import com.dizzycode.dizzycode.roommember.service.port.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomMemberRepositoryImpl implements RoomMemberRepository {

    private final RoomMemberJpaRepository roomMemberJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final RoomJpaRepository roomJpaRepository;

    @Override
    public List<Room> findRoomsByMemberId() {
        return roomMemberJpaRepository.findRoomsByMemberId(getMemberFromSession().getId()).stream()
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
    public RoomMember save(Long roomId) throws ClassNotFoundException {
        MemberEntity member = getMemberFromSession();
        RoomEntity room = roomJpaRepository.findByRoomId(roomId).orElseThrow(() -> new ClassNotFoundException("방이 존재하지 않습니다."));
        RoomMemberIdEntity roomMemberId = new RoomMemberIdEntity(member.getId(), room.getRoomId());
        RoomMemberEntity roomMember = new RoomMemberEntity();
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setRoom(room);
        roomMember.setMember(member);
        return roomMemberJpaRepository.save(roomMember).toModel();
    }

    @Override
    public void delete(RoomMember roomMember) {
        roomMemberJpaRepository.delete(RoomMemberEntity.fromModel(roomMember));
    }

    private MemberEntity getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberJpaRepository.findByEmail(email).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
    }
}
