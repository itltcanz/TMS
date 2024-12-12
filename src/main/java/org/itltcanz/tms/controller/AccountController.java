package org.itltcanz.tms.controller;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        var newAccount = accountService.register(account);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Account account) {
        var jwtToken = accountService.verify(account);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/profile")
    public ResponseEntity<Account> profile() {
        return ResponseEntity.ok(accountService.getCurrentUser());
    }
}
