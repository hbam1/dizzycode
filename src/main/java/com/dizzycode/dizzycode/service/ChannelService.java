package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Channel;
import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.dto.channel.ChannelCreateDTO;
import com.dizzycode.dizzycode.dto.channel.ChannelDetailDTO;
import com.dizzycode.dizzycode.repository.ChannelRepository;
import com.dizzycode.dizzycode.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final RoomRepository roomRepository;

    public ChannelDetailDTO createChannel(Long roomId, ChannelCreateDTO channelCreateDTO) {

        Room room = roomRepository.findByRoomId(roomId);

        Channel channel = new Channel();
        channel.setRoom(room);
        channel.setChannelName(channelCreateDTO.getChannelName());

        channel = channelRepository.save(channel);

        ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
        channelDetailDTO.setChannelId(channel.getChannelId());
        channelDetailDTO.setChannelName(channel.getChannelName());
        channelDetailDTO.setRoomId(channel.getRoom().getRoomId());

        return channelDetailDTO;
    }

    public List<ChannelDetailDTO> channelList(Long roomId) {

        Room room = roomRepository.findByRoomId(roomId);

        List<ChannelDetailDTO> channelDTOs = channelRepository.findChannelsByRoom(room).stream()
                .map(channel -> {
                    ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
                    channelDetailDTO.setChannelId(channel.getChannelId());
                    channelDetailDTO.setChannelName(channel.getChannelName());
                    channelDetailDTO.setRoomId(roomId);
                    return channelDetailDTO;
                })
                .collect(Collectors.toList());

        return channelDTOs;
    }

    public ChannelDetailDTO channelRetrieve(Long roomId, Long channelId) {
        Channel channel = channelRepository.findChannelByChannelId(channelId);
        ChannelDetailDTO channelDetailDTO = new ChannelDetailDTO();
        channelDetailDTO.setRoomId(roomId);
        channelDetailDTO.setChannelId(channel.getChannelId());
        channelDetailDTO.setChannelName(channel.getChannelName());

        return channelDetailDTO;
    }
}
