package com.dizzycode.dizzycode.security.controller;

import com.dizzycode.dizzycode.member.domain.SecondaryToken;
import com.dizzycode.dizzycode.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @GetMapping("/secondary-token")
    public ResponseEntity<SecondaryToken> getSecondaryToken() {

        return new ResponseEntity<>(securityService.secondaryToken(), HttpStatus.OK);
    }
}
