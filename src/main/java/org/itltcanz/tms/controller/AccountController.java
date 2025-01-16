package org.itltcanz.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.account.AccountAuthDto;
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
    @Operation(summary = "Update the priority of a task", description = "This endpoint allows you to update the priority of a task.")
    public ResponseEntity<AccountOutDto> register(@RequestBody @Valid AccountAuthDto accountAuthDto) {
        var accountOutDto = accountService.register(accountAuthDto);
        return ResponseEntity.ok(accountOutDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login to the service", description = "This endpoint allows you to login to the service.")
    public ResponseEntity<String> login(@RequestBody @Valid AccountAuthDto accountAuthDto) {
        var jwtToken = accountService.verify(accountAuthDto);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get account info", description = "This endpoint allows you to get your account information")
    public ResponseEntity<AccountOutDto> profile() {
        var accountEntity = accountService.getCurrentUser();
        var accountOutDto = modelMapper.map(accountEntity, AccountOutDto.class);
        return ResponseEntity.ok(accountOutDto);
    }
}
