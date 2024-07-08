package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.dto.room.DMRoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectMessageRoomService {

    public DMRoomCreateResponseDTO createDMRoom(DMRoomCreateDTO dmRoomCreateDTO) {

        return new DMRoomCreateResponseDTO();
    }
}
