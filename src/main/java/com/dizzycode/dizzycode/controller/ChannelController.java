package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.dto.channel.ChannelCreateDTO;
import com.dizzycode.dizzycode.dto.channel.ChannelDetailDTO;
import com.dizzycode.dizzycode.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/rooms/{roomId}/channels")
    public ResponseEntity<ChannelDetailDTO> createChannel(@RequestBody ChannelCreateDTO channelCreateDTO, @PathVariable Long roomId) {

        return new ResponseEntity<>(channelService.createChannel(roomId, channelCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/rooms/{roomId}/channels")
    public ResponseEntity<List<ChannelDetailDTO>> channelList(@PathVariable Long roomId) {

        return new ResponseEntity<>(channelService.channelList(roomId), HttpStatus.OK);
    }

    @GetMapping("/rooms/{roomId}/channels/{channelId}")
    public ResponseEntity<ChannelDetailDTO> channelRetrieve(@PathVariable Long channelId, @PathVariable Long roomId) {

        return new ResponseEntity<>(channelService.channelRetrieve(roomId, channelId), HttpStatus.OK);
    }
}
