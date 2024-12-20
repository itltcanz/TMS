package org.itltcanz.tms.controller;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.account.AccountInDto;
import org.itltcanz.tms.dto.account.AccountOutDto;
import org.itltcanz.tms.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    @PostMapping("/register")
    public ResponseEntity<AccountOutDto> register(@RequestBody AccountInDto accountInDto) {
        var accountOutDto = accountService.register(accountInDto);
        return ResponseEntity.ok(accountOutDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AccountInDto accountInDto) {
        var jwtToken = accountService.verify(accountInDto);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/profile")
    public ResponseEntity<AccountOutDto> profile() {
        var accountEntity = accountService.getCurrentUser();
        var accountOutDto = modelMapper.map(accountEntity, AccountOutDto.class);
        return ResponseEntity.ok(accountOutDto);
    }
}
