package com.dizzycode.dizzycode.channel.controller;

import com.dizzycode.dizzycode.channel.domain.dto.ChannelCreateDTO;
import com.dizzycode.dizzycode.channel.domain.dto.ChannelDetailDTO;
import com.dizzycode.dizzycode.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/rooms/{roomId}/categories/{categoryId}/channels")
    public ResponseEntity<ChannelDetailDTO> createChannel(@RequestBody ChannelCreateDTO channelCreateDTO, @PathVariable Long categoryId) {

        return new ResponseEntity<>(channelService.createChannel(categoryId, channelCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/rooms/{roomId}/categories/{categoryId}/channels")
    public ResponseEntity<List<ChannelDetailDTO>> channelList(@PathVariable Long categoryId) {

        return new ResponseEntity<>(channelService.channelList(categoryId), HttpStatus.OK);
    }

    @GetMapping("/rooms/{roomId}/categories/{categoryId}/channels/{channelId}")
    public ResponseEntity<ChannelDetailDTO> channelRetrieve(@PathVariable Long channelId, @PathVariable Long categoryId) {

        return new ResponseEntity<>(channelService.channelRetrieve(categoryId, channelId), HttpStatus.OK);
    }
}
